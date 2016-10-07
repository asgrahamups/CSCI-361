--Andrew Graham Assignment 6
--Haskell AStar Search
import System.IO
import Control.Monad
import Data.List 
import Data.List.Split
import System.Exit
--Code for parsing below this line
findById string cities = head (filter (\(V id _ _) -> id==string) cities)
removeSpaces [] = []
removeSpaces (x:xs) = if x == ' ' then removeSpaces xs else x: (removeSpaces xs)
removeSpacesFromList strings = map removeSpaces strings
main = do  
      putStrLn "Welcome to USA*, Haskell edition! Please input a file path to use."
      filePath <- getLine
      contents <- readFile filePath
      let
        linedData = (lines contents)
    	divided = (\(Just x) -> x)(elemIndex "" linedData)
        newContentsC = removeSpacesFromList (take divided linedData)
        newContentsR = removeSpacesFromList (drop divided linedData)
        parsedC = map words newContentsC
        parsedR = tail (map words newContentsR)
        cities = map stringsToVertex parsedC
        roads = map (stringsToRoads cities) parsedR
      putStrLn "The file has been loaded." 
      putStrLn "Please submit your query in the form of 'CityA-CityB', or press enter to exit."
      pois <-  getLine  
      let
      	poissplit = wordsBy(=='-') pois
      	start = head poissplit
      	dest = last poissplit
      	startCity = findById start cities
      	endCity = findById dest cities    
      	initialOpen = getAdjacent roads startCity [] 
      	path = aStarSearch roads startCity endCity [] initialOpen [] 0
      	superString = concat((getId startCity) : fst (extractCityNames path))
      	theLength = length (fst path)
      	onlyEdge = findEdge start dest roads
      	stringversion = start ++ " -> " ++ dest 
      	stringversiondist = (\(E (V _ _ _) (V _ _ _) a) -> a) onlyEdge
      if start == dest then do
      	putStrLn "Don't move" 
      	exitSuccess
      else do
      	putStrLn ""			  		
      if (theLength /= 0) then do	
      	putStr "in kilometers, the journey will take "
      	print (snd (extractCityNames path))
      	putStrLn superString
      else do
	  putStrLn stringversion
	  print stringversiondist
	  putStr "(in km)"

findEdge idOne idTwo graph = head [edge|edge<-graph , (getId (getFst edge) == idOne && getId (getSnd edge) == idTwo)]	  

stringsToRoads cities strings = E cityOne cityTwo weight
	where
	 cityOne = findById (strings !! 0) cities
	 cityTwo = findById (strings !! 1) cities
	 weight = (read (theWeight)) :: Float
	 theWeight = strings !! 2
	           
stringsToVertex strings = V id (read1,read2) 0 
 where
	 coord1 = strings !! 1
	 coord2 = strings !! 2
	 read1 = (read coord1)::Float
	 read2 = (read coord2)::Float
	 id = (strings !! 0)	 

extractCityNames (path,distance) = ( (firstGuy :(map getIds (map getEdge path))),distance)
	where
	 getIds = (\(E v1 v2 weight) -> " -> " ++ (getId v2))
	 firstGuy = " -> " ++ (getId (getFstEdgeScore (head path)))
 
--Code for main program below this line
data Vertex id coords score = V id coords score
	deriving (Eq, Show)	
data Edge a weight = E a a weight
	deriving (Eq, Show)
data Graph = Empty | Edge Graph --Graphs!
	deriving (Eq, Show)
data EdgeScore i edge =  S i edge
	deriving (Eq, Show)

getFstEdgeScore (S _ (E a b _)) = a
getSndEdgeScore (S _ (E a b _)) = b
getWgtEdgeScore (S _ (E _ _ a)) = a
getEdge (S _ (E a b weight)) = (E a b weight)
getEdgeScore (S i _ ) = i
getFst (E a _ _) = a
getSnd (E _ a _) = a
getWgt (E _ _ a) = a
setWgt (E a b c) d = (E a b d)
getId (V id _ _) = id
getCoords (V _ coords _) = coords
getScore (V _ _ score) = score

activeFirst active (E a b weight) 
 |isEqualVertex active a = E a b weight
 |otherwise = E b a weight
  
square x = x*x

distanceFormula coords1 coords2 = sqrt (squaredOne + squaredTwo)
	where
	 squaredOne = square ((fst coords1) - (fst coords2))
	 squaredTwo = square ((snd coords1) - (snd coords2))
 
isEqual edgeOne edgeTwo
 |getId (getFst edgeOne) == getId (getFst edgeTwo) && getId (getSnd edgeOne) == getId (getSnd edgeTwo) = True
 |otherwise = False 

isEqualVertex vOne vTwo = getId vOne == getId vTwo

aStarSearch roads start end visited open path rank
	|open == [] = error "No path found"
	|not(isEqualVertex start end) = aStarSearch newRoads newActive end newVisited newOpen newPath newRank
	|otherwise = (rankPath,distance)
		where
		  pathToTake = findMinimumScore open end
		  newActive = getSnd (pathToTake)
		  newVisited = pathToTake:visited
		  newRoads = replaceAllScores pathToTake roads --changes everything
		  newOpen = (filter(\x -> not(isEqual x pathToTake)) open) ++ getAdjacent newRoads newActive newVisited
		  newEdgeScore = S rank pathToTake
		  newRank = rank + 1
		  newPath = newEdgeScore:path
		  startEdgeScore = length path
		  rankPath = findRankPath (head path) path (last path)
		  distance = sum (map (getWgtEdgeScore) rankPath)
		  
compareRank scoreEdgeOne scoreEdgeTwo = getEdgeScore scoreEdgeOne >= getEdgeScore scoreEdgeTwo 
  		  
findRankPath startEdgeScore scoreEdges activeScoreEdge
 |startEdgeScore == activeScoreEdge = []
 |otherwise = addToPath
	where
		addToPath = [toChoose] ++ findRankPath startEdgeScore scoreEdges toChoose
		toChoose = chooseEdge scoreEdges activeScoreEdge --Find the next path we need to add
		newScoreEdges = filter (\x -> isEqual (getEdge toChoose) (getEdge x)) scoreEdges --remove that path from the possible paths we could take next 
		
chooseEdge path activeEdge = chooseBetween filteredList
	where
		filteredList = filter(\x -> isEqualVertex (getFstEdgeScore x) (getSndEdgeScore activeEdge)) path
		chooseBetween = foldr1 (\x y -> if (getEdgeScore x) > (getEdgeScore y) then x else y)
	  
findByIds edge edges = head (theFilter edges)
	where 
	 theFilter = filter (\x -> (isEqualVertex (getFst x) (getSnd edge) && isEqualVertex (getSnd x) (getFst edge)) || (isEqual edge x))

switch edgesWithScores edgesWithoutScores edge  = edgeWithoutScore
	where
		edgeWithScore = findByIds edge edgesWithScores
		edgeWithoutScore = findByIds edgeWithScore edgesWithoutScores
		
switchAll edgesWithScores edgesWithoutScores = nub ( map (switch edgesWithScores edgesWithoutScores) edgesWithScores)		

replaceVertexScoreInEdge newScore edge = (\(E a b w) -> (E newVert b w)) edge
	where
	 newVert = (\(V id coords score) -> (V id coords (newScore+oldScore))) (getFst edge)
	 oldScore = getScore (getFst (edge))
	
replaceAllScores _ [] = []
replaceAllScores pathToTake (y:ys) = newWeightsForEdge : replaceAllScores pathToTake ys
	where
	 newWeightsForEdge = if (isEqualVertex (getFst y) (getSnd pathToTake)) then replacement  else y
	 vertex = getSnd pathToTake
	 replacement = replaceVertexScoreInEdge pathWeight y
	 pathWeight = getWgt pathToTake

replaceVertexInRoads active roads = applyEdits roads
	where
	 applyEdits = map (\x-> if isEqual x active then replaceScore x else x)
	 replaceScore = (\(E a b wgt) -> (E a b ((getWgt active)+wgt)))

getAdjacent graph active visited = map (activeFirst active) adjacentOnes
	where
	 adjacentOnes = [edge| edge<-filteredGraph, isEqualVertex (getFst edge) active ||  isEqualVertex (getSnd edge) active]
	 filteredGraph = [edge | edge<-graph , not(elementInEdges edge visited)]

elementInEdges a [] = False 
elementInEdges edge (x:xs) = (isEqual edge x) || elementInEdges edge xs

findMinimumScore listEdges destination = findLowest listEdges
	where
	 findLowest =  foldr1 (\edge1 edge2 -> if findScore destination edge1 > findScore destination edge2 then edge2 else edge1)
	 
findScore destination edge  = getWgt edge + (hueristic (getSnd edge) destination) + getScore (getFst edge)	 

containsVertex [] _ = False
containsVertex (x:xs) vertex
 |getFst (x) == vertex = True
 |getSnd (x) == vertex = True
 |otherwise = False  
		   
hueristic vertexOne vertexTwo = distanceFormula (getCoords (vertexOne)) (getCoords (vertexTwo))