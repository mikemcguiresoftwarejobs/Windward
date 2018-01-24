try:
   f = open("input.txt")
except:
   f = open("day1/input.txt")
text = f.read()
counter=0
length=len(text)
for index in range(0,length/2-1):
   if text[index]==text[index+length/2]:
      counter+=int(text[index])
for index in range(length/2, length-1):
   if text[index]==text[index-length/2]:
      counter+=int(text[index])
print(counter)