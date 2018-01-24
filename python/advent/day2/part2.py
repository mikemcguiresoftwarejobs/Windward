
def read_input():
   try:
      f = open("input.txt")
   except:
      f = open("day2/input.txt")
   return f.read().split('\n')

checksum=0
for line in read_input():
   numbers = [int(char) for char in line.split()]
   for i in range(0,len(numbers)) :
      for j in range(0,len(numbers)) :
         if(i!=j and numbers[i]%numbers[j]==0):
            checksum += numbers[i]/numbers[j]
         if(i!=j and numbers[j]%numbers[i]==0):
            checksum += numbers[j]/numbers[i]
print(checksum/2)