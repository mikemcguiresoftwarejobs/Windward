
def read_input():
   try:
      f = open("input.txt")
   except:
      f = open("day5/input.txt")
   return f.read().split('\n')

instructions = [int(line) for line in read_input()]
index=0
numJumps=0
while(index<len(instructions)):
#   print("Step", numJumps, "current index:", index, "list: ", instructions)
   newIndex = index+instructions[index]
   instructions[index]+= -1 if instructions[index] >= 3 else 1
   index=newIndex
   numJumps+=1
print(numJumps)   
