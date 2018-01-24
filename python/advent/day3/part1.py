from sys import argv
import math

script, input = argv

print ("The input is", input)
numLayers=0
while((1+2*numLayers)**2<int(input)):
   numLayers+=1
inner_square_size=-1+2*numLayers
inner_square_size=inner_square_size**2
directions=["right", "top", "left", "bottom"]
index_within_layer = int(input)-inner_square_size-1
print(input, "is", numLayers, "layers out, being the sum of", inner_square_size, "and", int(input)-inner_square_size)
sidelength = 2*numLayers
direction_index = int(index_within_layer/sidelength)
print("direction in layer is", directions[direction_index], "as", index_within_layer, "completes", direction_index, "sides of length", sidelength)
center=inner_square_size+sidelength*direction_index+numLayers
distance=int(math.fabs((int)(input)-center))
print("distance from center of row or column is", distance, "as center of row or column is", center)
layers_fix=["left","down","right","up"][direction_index]
row_or_column_fix=["up","down","left","right","down","up","right","left"][2*direction_index+int(int(input)/(inner_square_size+sidelength*direction_index+numLayers))]
print("travel", numLayers, "units", layers_fix, "and", distance, "units", row_or_column_fix, "for a total of", numLayers+distance, "units")