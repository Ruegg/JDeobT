# JDeobT
JDeobT is an advanced String Deobfuscator/Decrypter for obfuscated classes or jars. JDeobT takes in 3 different 
filetypes to deobfuscate, these are .jad, .class, and .jar. JDeobT uses the JAD disassembler to disassemble clients 
if you are decompiling from a class file or jar file. If you would like to use the JDeobT function to deobfuscate 
full jars or classes, you will need to place the jad.exe file in the same directory as JDeobT.jar.

<b>Usage</b>

'java -jar JDeobT.jar' - Once the jar is run from the OS command line, you will now be in the JDeobT command line
Usage

<i>target.jar -type -transformer</i>

Example: C:/Users/Steve/Test.jar -jar -zkm

TYPE:

-<b>jar</b>   String deobfuscate a whole jar file

-<b>class</b>   Only string deobfuscate a single class file

-<b>jad</b>   Use this type if your target is a already disassembled by JAD

TRANSFORMER:

Available transformers are ZKM and Allatori

<b>Dependencies</b>


Optional: jad.exe - Needed for string deobfuscating jars and classes
