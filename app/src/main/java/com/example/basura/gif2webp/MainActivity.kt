package com.example.basura.gif2webp

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.basura.gif2webp.databinding.ActivityMainBinding
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import java.net.URL
import java.nio.file.spi.FileTypeDetector


class MainActivity : AppCompatActivity() {

    /**
     * A native method that is implemented by the 'gif2webp' native library,
     * which is packaged with this application.
     */
    private external fun nativegif2webpExecute(arguments: Array<String>): Int

    companion object {
        // Used to load the 'gif2webp' library on application startup.
        init {
            System.loadLibrary("gif2webp")
        }
    }

    var ImageUrl: String = "https://www.animatedimages.org/data/media/209/animated-cat-image-0072.gif"
    //var ImageUrl: String = "https://upload.wikimedia.org/wikipedia/commons/2/2c/Rotating_earth_%28large%29.gif"
    //var ImageUrl: String = "https://upload.wikimedia.org/wikipedia/commons/e/e5/Cherry-blosom-draw-background-semiflowers-8.gif"
    //var ImageUrl: String = "https://upload.wikimedia.org/wikipedia/commons/4/47/PNG_transparency_demonstration_1.png"


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Example of a call to a native method
        //binding.sampleText.text = stringFromJNI()

        //imageUrl = binding.imageUrl.text.toString()
        binding.imageUrl.setText(ImageUrl.trim())



        binding.convertBtn.setOnClickListener {
            binding.webpImage.visibility = View.VISIBLE

            gif2webp("prueba.gif", "prueba.webp")
        }

        binding.downloadBtn.setOnClickListener {

            // Descar la imagen gif
            val url = binding.imageUrl.text.toString()

            if (url.substringAfterLast('.', "") == "gif") {
                download_image(url, baseContext)
            } else {
                binding.status.text = "No es un archivo .gif"
                binding.status.setTextColor(Color.RED)
            }

            // Elimina la imagen webp y deja de mostrarla
            val imgFile = File(baseContext.filesDir, "prueba.webp")

            if (imgFile.exists()) {
                imgFile.delete()
            }

            binding.webpImage.setImageBitmap(null)
            binding.webpPathText.text = ""

        }

        binding.deleteBtn.setOnClickListener {
            // Elimina el gif y deja de mostrarlo
            val imgFile = File(baseContext.filesDir, "prueba.gif")

            if (imgFile.exists()) {
                imgFile.delete()
            }

            binding.gifImage.setImageBitmap(null)
            binding.gifPathText.text = ""

            binding.deleteBtn.isEnabled = false

            binding.webpImage.setImageBitmap(null)
            binding.webpPathText.text = ""

            binding.convertBtn.isEnabled = false
            binding.status.text = ""
        }

        if (File(baseContext.filesDir, "prueba.gif").exists()) {
            load_gif_image("prueba.gif")

        } else {
            binding.deleteBtn.isEnabled = false
            binding.convertBtn.isEnabled = false
        }
    }

    fun download_image(my_url: String, context: Context) {

        Thread(Runnable {
            val url = URL(my_url)
            var `in`: InputStream

            try {
                `in`  = BufferedInputStream(url.openStream())
            } catch (e: FileNotFoundException) {
                // handler
                runOnUiThread {
                    binding.status.text = "Error 404: No se encontro el archivo"
                    binding.status.setTextColor(Color.RED)
                }

                return@Runnable
            }

            val out = ByteArrayOutputStream()
            val buf = ByteArray(1024)

            var n: Int

            while (-1 != `in`.read(buf).also { n = it }) {
                out.write(buf, 0, n)
            }

            `in`.close()

            val response: ByteArray = out.toByteArray()

            //val fos = FileOutputStream("urltoimage")
            //fos.write(response)
            //fos.close()

            // NOTA: podria hacer esto directamente y sin la conversion a string...
            val file_type = out.toString()
            if (file_type.contains("GIF89a") || file_type.contains("GIF87a")) {
                val filename = "prueba.gif"

                context.openFileOutput(filename, Context.MODE_PRIVATE).use {
                    it.write(response)
                }

                // ask the UI thread to do the updates
                runOnUiThread {
                    load_gif_image(filename)

                    binding.deleteBtn.isEnabled = true

                    binding.convertBtn.isEnabled = true
                }

            } else {
                runOnUiThread {
                    binding.status.text = "No es un archivo con formato gif"
                    binding.status.setTextColor(Color.RED)
                }
            }

        }).start()

    }

    fun load_gif_image(name: String): String {
        val imgFile = File(baseContext.filesDir, name)

        if (imgFile.exists() && imgFile.isFile()) {
            // Load a image from the gif file
            //val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            //val myImage = binding.gifImage
            //myImage.setImageBitmap(myBitmap)


            Glide.with(baseContext).load(imgFile).into(binding.gifImage)

            binding.gifPathText.text = imgFile.absolutePath

            return imgFile.absolutePath

        } else {
            binding.gifPathText.text = "No existe el archivo gif"
        }

        return ""
    }

    fun gif2webp(gif_file: String, output_name: String) {
        val imgDir = baseContext.filesDir.path

        binding.webpPathText.text = imgDir

        // -mixed empeora el rendimiento en gif livianos pero lo mejora bastante en los mas pesados (>= 1MB)
        val my_arguments = arrayOf("-mt", "-loop_compatibility", "-quiet", "$imgDir/$gif_file", "-o", "$imgDir/$output_name")

        measureTimeMillis({ time -> binding.status.text = "Read and conversion took $time ms"; binding.status.setTextColor(Color.GREEN) }) {
                nativegif2webpExecute(my_arguments)
            }


        val gifFile = File(baseContext.filesDir, gif_file)
        val webpFile = File(baseContext.filesDir, output_name)

        if (webpFile.exists() && gifFile.exists()) {
            binding.webpPathText.text = webpFile.path

            binding.status.append(" - ${gifFile.length() / 1_000.0} kB to ${webpFile.length() / 1_000.0} kB")

            // Load a image from the webp file
            //val myBitmap = BitmapFactory.decodeFile(webpFile.absolutePath)
            //binding.webpImage.setImageBitmap(myBitmap)
            Glide.with(baseContext).load(webpFile).into(binding.webpImage)
        }
    }

    inline fun <T> measureTimeMillis(loggingFunction: (Long) -> Unit,
                                     function: () -> T): T {

        val startTime = System.currentTimeMillis()
        val result: T = function.invoke()
        loggingFunction.invoke(System.currentTimeMillis() - startTime)

        return result
    }
}