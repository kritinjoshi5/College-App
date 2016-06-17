public class MainActivity extends ActionBaActivity{
	private static final String TAG = MainActivity.class.getCanonicalName();
	private WebView mWebView;
	private ProgressBar mLoading;
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mWebView = (WebView)findViewById(R.id.wvPortal);
		mLoading = (ProgressBar)findViewById(R.id.pbLoading);
		mWebView.loadUrl("file:///android_asset/www/index.html");
		WebSettings mWebSettings = mWebView.getSettings();
		mWebSettings.setJavaScriptEnabled(true);
		mWebView.setWebChromeClient(new BridgeWCClient());
		
	}
	private void processCommand(JSONObject commandJSON)
	 throws JSONException
	 {
	 	String command = commandJSON.getString("method");
	 	if("login".equals(command))
	 	{
	 		int state = commandJSON.getInt("state");
	 		if(state==0)
	 		{
	 			MainActivity.this.runOnUiThread(new Runnable()
	 			{
	 				@Override
	 				public void run()
	 				{
	 					mLoading.setVisiblity(View.VISIBLE);
	 				}
	 			});
	 		}
	 		else if(state==1)
	 		{
	 			MainActivity.this.runOnUiThread(new Runnable()
	 			{   @Override public void run()
	 				{
	 					mLoading.setVisiblity(View.GONE);
	 				}
	 			});
	 		}
	 	}
	 }
}
private class BridgeWCClient extends WebChromeClient
{
	@Override
	public
	boolean
	onJsPrompt(WebView view, String url, String title, String message, JsPromptResult result)
	{
		if(title.equals(BRIDGE_KEY))
		{
			JSONObject commandJSON = null;
			try
			{
					commandJSON = new JSONObject(message);
					processCommand(commandJSON);
			}catch(JSONExceptionex)
			{
										Log.e(TAG,"Invalid JSON:"+ex.getMessage());
										result.confirm();
										return true;
			}result.confirm();
			 return true;
		}
		else
		{
			return false;
		}
	}
}
