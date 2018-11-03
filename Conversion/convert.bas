Type=StaticCode
Version=6.5
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
'Code module
'Subs in this code module will be accessible from all modules.
Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
End Sub

Sub decTobin(decVal As Int) As String 
Dim bits As Int, dec2bin As StringBuilder 
dec2bin.Initialize 
Do While decVal > Power(2,bits)-1
	bits=bits+1
Loop 
For i =bits-1 To 0 Step -1
	dec2bin.Append((Bit.And(decVal , Power(2,i)))/Power(2,i))
Next
Return dec2bin
End Sub

Sub binTodec(binVal As String) As Int
Dim i As Int, v As Int, dec As Int 
i = binVal.Length
v = 1
For pos = i To 1 Step -1
	If binVal.SubString2(pos-1, pos) = "1" Then dec = dec + v
v = v * 2
	Next
	Return dec
End Sub
