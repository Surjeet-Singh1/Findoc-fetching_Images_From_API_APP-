package com.example.assigmentapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random // <<< Import Random

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var sliderImageAdapter: ImageAdapter
    private val sliderImageList = listOf(
        R.drawable.img,
        R.drawable.img_1,
        R.drawable.img_3,
        R.drawable.img_4
    )

    private val scrollHandler = Handler(Looper.getMainLooper())
    private val scrollDelay: Long = 3000
    private val autoScrollRunnable = object : Runnable {
        override fun run() {
            if (sliderImageList.isNotEmpty()) {
                var currentItem = viewPager.currentItem
                currentItem = (currentItem + 1) % sliderImageList.size
                viewPager.setCurrentItem(currentItem, true)
                scrollHandler.postDelayed(this, scrollDelay)
            }
        }
    }

    private lateinit var exploreImagesButton: Button
    private lateinit var fetchedImagesRecyclerView: RecyclerView
    private lateinit var fetchedImageAdapter: FetchedImageAdapter

    // Keep track of the last fetched page to avoid accidentally re-fetching the same random page immediately
    // Or simply always fetch a new random page. For simplicity, let's fetch a new random page each time.
    // private var lastFetchedPage = 0

    private val picsumApiService: PicsumApiService by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl("https://picsum.photos/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PicsumApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val companylogo=findViewById<ImageView>(R.id.company)

        companylogo.setOnClickListener {
            Toast.makeText(this, "Forage Images App", Toast.LENGTH_SHORT).show()

        }
        viewPager = findViewById(R.id.viewPager)
        sliderImageAdapter = ImageAdapter(sliderImageList)
        viewPager.adapter = sliderImageAdapter

        fetchedImagesRecyclerView = findViewById(R.id.fetchedImagesRecyclerView)
        fetchedImagesRecyclerView.layoutManager = LinearLayoutManager(this)
        fetchedImageAdapter = FetchedImageAdapter(emptyList())
        fetchedImagesRecyclerView.adapter = fetchedImageAdapter

        exploreImagesButton = findViewById(R.id.exploreImagesButton)
        exploreImagesButton.setOnClickListener {
            fetchPicsumImages()
        }
    }

    private fun fetchPicsumImages() {
        // Generate a random page number.
        // Picsum has many pages. A range like 1 to 100 should give good variety for 10 images per page.
        // You can adjust the upper bound if needed.
        val randomPageToFetch = Random.nextInt(1, 101) // Generates a random Int between 1 (inclusive) and 101 (exclusive) -> 1 to 100

        // Optional: Add a loading indicator here (e.g., show a ProgressBar)
        exploreImagesButton.isEnabled = false // Disable button while fetching
        Toast.makeText(this, "Fetching new images...", Toast.LENGTH_SHORT).show()


        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Use the randomPageToFetch in the API call
                val response = picsumApiService.getImages(page = randomPageToFetch, limit = 10)
                if (response.isSuccessful) {
                    val images = response.body()
                    withContext(Dispatchers.Main) {
                        exploreImagesButton.isEnabled = true // Re-enable button
                        if (images != null && images.isNotEmpty()) { // Good practice to also check isNotEmpty
                            fetchedImageAdapter.updateData(images) // Replace data with new images
                            Toast.makeText(this@MainActivity, "Fetched ${images.size} images from page $randomPageToFetch", Toast.LENGTH_SHORT).show()
                        } else {
                            Log.e("MainActivity", "Response body is null or empty for page $randomPageToFetch")
                            Toast.makeText(this@MainActivity, "No images found on page $randomPageToFetch", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        exploreImagesButton.isEnabled = true // Re-enable button
                        Log.e("MainActivity", "Error fetching images: ${response.code()} - ${response.message()}")
                        Toast.makeText(this@MainActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    exploreImagesButton.isEnabled = true // Re-enable button
                    Log.e("MainActivity", "Exception fetching images", e)
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (sliderImageList.isNotEmpty()) {
            scrollHandler.postDelayed(autoScrollRunnable, scrollDelay)
        }
    }

    override fun onPause() {
        super.onPause()
        scrollHandler.removeCallbacks(autoScrollRunnable)
    }
}
