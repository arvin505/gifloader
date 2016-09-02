#gifloader
this is an simple & lightweight to load gif from internet for android.<br>
it use memorycache defaultly, and you can use your own cache by implment GifCache<br>
and you can also use MemoryCache，WeakMemoryCache DiskCache and DoubleCache><br>
MemoryCache using LruCache<br>
WeakMemoryCache using WeakReference<br>
DisCache using DiskLruCache<br>
DoubleCache contans Memory & disk, and you need to new instance width assignating Memory cache<br>
				for examle:<br>
					DoubleCache = new DoubleCache(appContext,new WeakMemoryCache());




#Installation
	repositories {
		jcenter()
	}

	dependencies {<br>
  	  compile('com.guardanis:imageloader:1.3.3')<br>
	}
	
#Usage
	you can use gifview in you layou file or through constructor as an normal view
		<com.arvin.gifloader.widget.GifView
        	android:id="@+id/gif"
	        android:layout_width="match_parent"
	        android:layout_height="match_parent" />
  
  
 	at first, you should init in you application 
 	
 			GifLoaderConfig config = new GifLoaderConfig();
    		config.setCache(new DoubleCache(this, new MemoryCache())).
        	       setThreadCount(5);
  		   	GifLoader.getInstance().init(config);
        	
  	and displayGif width
  			GifLoader.getInstance().displayGif(gifView, "your gif url");
