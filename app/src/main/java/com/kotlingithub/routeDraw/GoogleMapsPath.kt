package com.kotlingithub.routeDraw

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.util.Log

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.kotlingithub.R

import org.json.JSONObject

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList
import java.util.HashMap

class GoogleMapsPath(
    private val context: Context,
    var map: GoogleMap,
    private val origin: LatLng,
    dest: LatLng
) {

    init {
        val url = getDirectionsUrl(origin, dest)
        val FetchUrl = FetchUrl()
        FetchUrl.execute(url)

    }

    @Throws(IOException::class)
    private fun downloadUrl(strUrl: String): String {
        var data = ""
        var iStream: InputStream? = null
        var urlConnection: HttpURLConnection? = null
        try {
            val url = URL(strUrl)

            // Creating an http connection to communicate with url
            urlConnection = url.openConnection() as HttpURLConnection

            // Connecting to url
            urlConnection.connect()

            // Reading data from url
            iStream = urlConnection.inputStream

            val br = BufferedReader(InputStreamReader(iStream))

            val sb = StringBuffer()

            val line = ""
            while ((br.readLine()) != null) {
                sb.append(line)
            }

            data = sb.toString()
            Log.d("downloadUrl", data)
            br.close()

        } catch (e: Exception) {
            Log.d("Exception", e.toString())
        } finally {
            iStream!!.close()
            urlConnection!!.disconnect()
        }
        return data
    }

    private fun getDirectionsUrl(origin: LatLng, dest: LatLng): String {

        // Origin of route
        val str_origin = "origin=" + origin.latitude + "," + origin.longitude

        // Destination of route
        val str_dest = "destination=" + dest.latitude + "," + dest.longitude

        // Sensor enabled
        val sensor = "sensor=false"

        val api_key = context.getString(R.string.google_map_key)

        // Building the parameters to the web service
        val parameters = "$str_origin&$str_dest&$sensor&key=$api_key"

        // Output format
        val output = "json"

        // Building the url to the web service
        val url = "https://maps.googleapis.com/maps/api/directions/$output?$parameters"
        Log.d("mapUrl", "getDirectionsUrl: map url : $url")
        return url
    }

    // Fetches data from url passed
    @SuppressLint("StaticFieldLeak")
    private inner class FetchUrl : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg url: String): String? {

            // For storing data from web service
            var data = ""

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0])
                Log.d("Background Task data", data)
            } catch (e: Exception) {
                Log.d("Background Task", e.toString())
            }

            return data
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)

            val parserTask = ParserTask()

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result)

        }
    }

    private inner class ParserTask : AsyncTask<String, Int, List<List<HashMap<String, String>>>>() {

        // Parsing the data in non-ui thread
        override fun doInBackground(vararg jsonData: String): List<List<HashMap<String, String>>>? {

            val jObject: JSONObject
            var routes: List<List<HashMap<String, String>>>? = null

            try {
                jObject = JSONObject(jsonData[0])
                Log.d("ParserTask", jsonData[0])
                val parser = DirectionsJSONParser()
                Log.d("ParserTask", parser.toString())

                // Starts parsing data
                routes = parser.parse(jObject)
                Log.d("ParserTask", "Executing routes")
                Log.d("ParserTask", routes.toString())

            } catch (e: Exception) {
                Log.d("ParserTask", e.toString())
                e.printStackTrace()
            }

            return routes
        }

        // Executes in UI thread, after the parsing process
        override fun onPostExecute(result: List<List<HashMap<String, String>>>) {
            var points: ArrayList<LatLng>
            var lineOptions: PolylineOptions? = null

            // Traversing through all the routes
            for (i in result.indices) {
                points = ArrayList()
                lineOptions = PolylineOptions()

                // Fetching i-th route
                val path = result[i]

                // Fetching all the points in i-th route
                for (j in path.indices) {
                    val point = path[j]

                    val lat = java.lang.Double.parseDouble(point["lat"]!!)
                    val lng = java.lang.Double.parseDouble(point["lng"]!!)
                    val position = LatLng(lat, lng)

                    points.add(position)
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points)
                lineOptions.width(8f)
                lineOptions.color(R.color.colorAccent)

                Log.d("onPostExecute", "onPostExecute lineoptions decoded")

            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                map.addPolyline(lineOptions)
                map.moveCamera(CameraUpdateFactory.newLatLng(origin))
            } else {
                Log.d("onPostExecute", "without Polylines drawn")
            }
        }
    }
}