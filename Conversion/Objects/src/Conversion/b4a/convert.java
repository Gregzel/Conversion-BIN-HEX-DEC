package Conversion.b4a;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class convert {
private static convert mostCurrent = new convert();
public static Object getObject() {
    throw new RuntimeException("Code module does not support this method.");
}
 public anywheresoftware.b4a.keywords.Common __c = null;
public Conversion.b4a.main _main = null;
public static int  _bintodec(anywheresoftware.b4a.BA _ba,String _binval) throws Exception{
int _i = 0;
int _v = 0;
int _dec = 0;
int _pos = 0;
 //BA.debugLineNum = 20;BA.debugLine="Sub binTodec(binVal As String) As Int";
 //BA.debugLineNum = 21;BA.debugLine="Dim i As Int, v As Int, dec As Int";
_i = 0;
_v = 0;
_dec = 0;
 //BA.debugLineNum = 22;BA.debugLine="i = binVal.Length";
_i = _binval.length();
 //BA.debugLineNum = 23;BA.debugLine="v = 1";
_v = (int) (1);
 //BA.debugLineNum = 24;BA.debugLine="For pos = i To 1 Step -1";
{
final int step4 = (int) (-1);
final int limit4 = (int) (1);
for (_pos = _i ; (step4 > 0 && _pos <= limit4) || (step4 < 0 && _pos >= limit4); _pos = ((int)(0 + _pos + step4)) ) {
 //BA.debugLineNum = 25;BA.debugLine="If binVal.SubString2(pos-1, pos) = \"1\" Then dec =";
if ((_binval.substring((int) (_pos-1),_pos)).equals("1")) { 
_dec = (int) (_dec+_v);};
 //BA.debugLineNum = 26;BA.debugLine="v = v * 2";
_v = (int) (_v*2);
 }
};
 //BA.debugLineNum = 28;BA.debugLine="Return dec";
if (true) return _dec;
 //BA.debugLineNum = 29;BA.debugLine="End Sub";
return 0;
}
public static String  _dectobin(anywheresoftware.b4a.BA _ba,int _decval) throws Exception{
int _bits = 0;
anywheresoftware.b4a.keywords.StringBuilderWrapper _dec2bin = null;
int _i = 0;
 //BA.debugLineNum = 8;BA.debugLine="Sub decTobin(decVal As Int) As String";
 //BA.debugLineNum = 9;BA.debugLine="Dim bits As Int, dec2bin As StringBuilder";
_bits = 0;
_dec2bin = new anywheresoftware.b4a.keywords.StringBuilderWrapper();
 //BA.debugLineNum = 10;BA.debugLine="dec2bin.Initialize";
_dec2bin.Initialize();
 //BA.debugLineNum = 11;BA.debugLine="Do While decVal > Power(2,bits)-1";
while (_decval>anywheresoftware.b4a.keywords.Common.Power(2,_bits)-1) {
 //BA.debugLineNum = 12;BA.debugLine="bits=bits+1";
_bits = (int) (_bits+1);
 }
;
 //BA.debugLineNum = 14;BA.debugLine="For i =bits-1 To 0 Step -1";
{
final int step6 = (int) (-1);
final int limit6 = (int) (0);
for (_i = (int) (_bits-1) ; (step6 > 0 && _i <= limit6) || (step6 < 0 && _i >= limit6); _i = ((int)(0 + _i + step6)) ) {
 //BA.debugLineNum = 15;BA.debugLine="dec2bin.Append((Bit.And(decVal , Power(2,i)))/Pow";
_dec2bin.Append(BA.NumberToString((anywheresoftware.b4a.keywords.Common.Bit.And(_decval,(int) (anywheresoftware.b4a.keywords.Common.Power(2,_i))))/(double)anywheresoftware.b4a.keywords.Common.Power(2,_i)));
 }
};
 //BA.debugLineNum = 17;BA.debugLine="Return dec2bin";
if (true) return BA.ObjectToString(_dec2bin);
 //BA.debugLineNum = 18;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 3;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 6;BA.debugLine="End Sub";
return "";
}
}
