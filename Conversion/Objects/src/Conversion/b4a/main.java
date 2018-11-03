package Conversion.b4a;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = false;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFirst) {
			processBA = new BA(this.getApplicationContext(), null, null, "Conversion.b4a", "Conversion.b4a.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
				p.finish();
			}
		}
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		mostCurrent = this;
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(processBA, wl, true))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "Conversion.b4a", "Conversion.b4a.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "Conversion.b4a.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEvent(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return main.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null) //workaround for emulator bug (Issue 2423)
            return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        processBA.setActivityPaused(true);
        mostCurrent = null;
        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
			if (mostCurrent == null || mostCurrent != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (main) Resume **");
		    processBA.raiseEvent(mostCurrent._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        Object[] o;
        if (permissions.length > 0)
            o = new Object[] {permissions[0], grantResults[0] == 0};
        else
            o = new Object[] {"", false};
        processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public anywheresoftware.b4a.objects.LabelWrapper _label4 = null;
public anywheresoftware.b4a.objects.PanelWrapper _binairedecimal = null;
public anywheresoftware.b4a.objects.LabelWrapper _label1 = null;
public anywheresoftware.b4a.objects.EditTextWrapper _nombredec = null;
public anywheresoftware.b4a.objects.ButtonWrapper _conversiondec = null;
public anywheresoftware.b4a.objects.LabelWrapper _label3 = null;
public anywheresoftware.b4a.objects.LabelWrapper _resultatdec = null;
public anywheresoftware.b4a.objects.LabelWrapper _label7 = null;
public anywheresoftware.b4a.objects.LabelWrapper _resultatdechex = null;
public anywheresoftware.b4a.objects.LabelWrapper _label5 = null;
public anywheresoftware.b4a.objects.PanelWrapper _decimalbinaire = null;
public anywheresoftware.b4a.objects.LabelWrapper _label2 = null;
public anywheresoftware.b4a.objects.EditTextWrapper _nombrebin = null;
public anywheresoftware.b4a.objects.ButtonWrapper _conversionbin = null;
public anywheresoftware.b4a.objects.LabelWrapper _label6 = null;
public anywheresoftware.b4a.objects.LabelWrapper _resultatbin = null;
public anywheresoftware.b4a.objects.LabelWrapper _label8 = null;
public anywheresoftware.b4a.objects.LabelWrapper _resultatbinhex = null;
public Conversion.b4a.convert _convert = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 49;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 52;BA.debugLine="Activity.LoadLayout(\"IHM_conversion\")";
mostCurrent._activity.LoadLayout("IHM_conversion",mostCurrent.activityBA);
 //BA.debugLineNum = 53;BA.debugLine="End Sub";
return "";
}
public static String  _bin2hex(String _bnumber) throws Exception{
 //BA.debugLineNum = 89;BA.debugLine="Sub Bin2Hex(bnumber As String) As String";
 //BA.debugLineNum = 90;BA.debugLine="Return Bit.ToHexString(Bit.ParseInt(bnumber,2))";
if (true) return anywheresoftware.b4a.keywords.Common.Bit.ToHexString(anywheresoftware.b4a.keywords.Common.Bit.ParseInt(_bnumber,(int) (2)));
 //BA.debugLineNum = 91;BA.debugLine="End Sub";
return "";
}
public static String  _conversionbin_click() throws Exception{
int _dec = 0;
String _hex = "";
 //BA.debugLineNum = 66;BA.debugLine="Sub ConversionBin_Click";
 //BA.debugLineNum = 68;BA.debugLine="Dim Dec As Int";
_dec = 0;
 //BA.debugLineNum = 69;BA.debugLine="Dim Hex As String";
_hex = "";
 //BA.debugLineNum = 70;BA.debugLine="If NombreBin.Text=\"\" Then NombreBin.Text=0";
if ((mostCurrent._nombrebin.getText()).equals("")) { 
mostCurrent._nombrebin.setText((Object)(0));};
 //BA.debugLineNum = 71;BA.debugLine="Dec=convert.binTodec(NombreBin.Text)";
_dec = mostCurrent._convert._bintodec(mostCurrent.activityBA,mostCurrent._nombrebin.getText());
 //BA.debugLineNum = 72;BA.debugLine="ResultatBin.Text=Dec";
mostCurrent._resultatbin.setText((Object)(_dec));
 //BA.debugLineNum = 73;BA.debugLine="Hex=Bin2Hex(NombreBin.Text)";
_hex = _bin2hex(mostCurrent._nombrebin.getText());
 //BA.debugLineNum = 74;BA.debugLine="ResultatBinHex.Text=Hex";
mostCurrent._resultatbinhex.setText((Object)(_hex));
 //BA.debugLineNum = 75;BA.debugLine="End Sub";
return "";
}
public static String  _conversiondec_click() throws Exception{
int _bin = 0;
String _hex = "";
 //BA.debugLineNum = 55;BA.debugLine="Sub ConversionDec_Click";
 //BA.debugLineNum = 57;BA.debugLine="Dim Bin As Int";
_bin = 0;
 //BA.debugLineNum = 58;BA.debugLine="Dim Hex As String";
_hex = "";
 //BA.debugLineNum = 59;BA.debugLine="If NombreDec.Text=\"\" Then NombreDec.Text=0";
if ((mostCurrent._nombredec.getText()).equals("")) { 
mostCurrent._nombredec.setText((Object)(0));};
 //BA.debugLineNum = 60;BA.debugLine="Bin=convert.decTobin(NombreDec.Text) 'Conversion";
_bin = (int)(Double.parseDouble(mostCurrent._convert._dectobin(mostCurrent.activityBA,(int)(Double.parseDouble(mostCurrent._nombredec.getText())))));
 //BA.debugLineNum = 61;BA.debugLine="ResultatDec.Text=Bin 'Affichage de la conversion";
mostCurrent._resultatdec.setText((Object)(_bin));
 //BA.debugLineNum = 62;BA.debugLine="Hex=DectoHex(NombreDec.Text)";
_hex = _dectohex((int)(Double.parseDouble(mostCurrent._nombredec.getText())));
 //BA.debugLineNum = 63;BA.debugLine="ResultatDecHex.Text=Hex";
mostCurrent._resultatdechex.setText((Object)(_hex));
 //BA.debugLineNum = 64;BA.debugLine="End Sub";
return "";
}
public static String  _dectohex(int _n) throws Exception{
String _chars = "";
String _hex = "";
 //BA.debugLineNum = 78;BA.debugLine="Sub DectoHex(n As Int) As String";
 //BA.debugLineNum = 79;BA.debugLine="Dim chars As  String =\"0123456789ABCDEF\"";
_chars = "0123456789ABCDEF";
 //BA.debugLineNum = 80;BA.debugLine="Dim hex As String = \"\"";
_hex = "";
 //BA.debugLineNum = 81;BA.debugLine="Do While n > 0";
while (_n>0) {
 //BA.debugLineNum = 82;BA.debugLine="hex = chars.charAt(n Mod 16) & hex";
_hex = BA.ObjectToString(_chars.charAt((int) (_n%16)))+_hex;
 //BA.debugLineNum = 83;BA.debugLine="n = n / 16";
_n = (int) (_n/(double)16);
 }
;
 //BA.debugLineNum = 85;BA.debugLine="Return hex";
if (true) return _hex;
 //BA.debugLineNum = 86;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 21;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 26;BA.debugLine="Private Label4 As Label";
mostCurrent._label4 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Private BinaireDecimal As Panel";
mostCurrent._binairedecimal = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Private Label1 As Label";
mostCurrent._label1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Private NombreDec As EditText";
mostCurrent._nombredec = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Private ConversionDec As Button";
mostCurrent._conversiondec = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Private Label3 As Label";
mostCurrent._label3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Private ResultatDec As Label";
mostCurrent._resultatdec = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Private Label7 As Label";
mostCurrent._label7 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Private ResultatDecHex As Label";
mostCurrent._resultatdechex = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 37;BA.debugLine="Private Label5 As Label";
mostCurrent._label5 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 38;BA.debugLine="Private DecimalBinaire As Panel";
mostCurrent._decimalbinaire = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 39;BA.debugLine="Private Label2 As Label";
mostCurrent._label2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 40;BA.debugLine="Private NombreBin As EditText";
mostCurrent._nombrebin = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Private ConversionBin As Button";
mostCurrent._conversionbin = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 42;BA.debugLine="Private Label6 As Label";
mostCurrent._label6 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 43;BA.debugLine="Private ResultatBin As Label";
mostCurrent._resultatbin = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 44;BA.debugLine="Private Label8 As Label";
mostCurrent._label8 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 45;BA.debugLine="Private ResultatBinHex As Label";
mostCurrent._resultatbinhex = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 47;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
convert._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 15;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 19;BA.debugLine="End Sub";
return "";
}
}
