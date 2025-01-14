package com.dss.carritocompra

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.util.TileSystem

class MapsActivity : AppCompatActivity() {
    private lateinit var map: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configurar osmdroid
        val sharedPrefs = applicationContext.getSharedPreferences("osmdroid", MODE_PRIVATE)
        Configuration.getInstance().load(this, sharedPrefs)

        // Establecer el layout
        setContentView(R.layout.activity_maps)

        // Inicializar el mapa
        map = findViewById(R.id.map)
        map.setMultiTouchControls(true)
        map.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK)

        // Configurar posición inicial (ETSIIT)
        val startPoint = GeoPoint(37.197766, -3.624808) // ETSIIT
        map.controller.setZoom(18.0)
        map.controller.setCenter(startPoint)

        // Crear un marcador para ETSIIT con un icono personalizado
        val marker = Marker(map)
        marker.position = startPoint
        marker.title = "ETSIIT - Universidad de Granada"

        // Asignar una imagen personalizada para simular la posición del usuario
        marker.icon = resources.getDrawable(R.drawable.user_location) // Usar el ícono en drawable

        map.overlays.add(marker)

        // Añadir otros almacenes
        val almacenes = listOf(
            GeoPoint(37.1990, -3.6255), // Almacén 1
            GeoPoint(37.1970, -3.6235), // Almacén 2
            GeoPoint(37.1960, -3.6270), // Almacén 3
            GeoPoint(37.1950, -3.6220)  // Almacén 4
        )

        for ((index, almacen) in almacenes.withIndex()) {
            val almacenMarker = Marker(map)
            almacenMarker.position = almacen
            almacenMarker.title = "Almacén ${index + 1}"
            map.overlays.add(almacenMarker)
        }
    }

    override fun onResume() {
        super.onResume()
        map.onResume() // Reanuda el mapa
    }

    override fun onPause() {
        super.onPause()
        map.onPause() // Pausa el mapa
    }
}
