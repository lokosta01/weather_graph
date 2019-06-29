package com.example.sokol.weather_test.ui.fragment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.ViewCompat
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.example.sokol.weather_test.R
import com.example.sokol.weather_test.realm.model.WeatherData
import com.example.sokol.weather_test.ui.fragment.weather_list.WeatherListFragmentDirections
import com.example.sokol.weather_test.utils.navigate

class WeatherListAdapter(val context: Context, val view: View, private var listener: CallbackListener) :
        RecyclerView.Adapter<WeatherListAdapter.ViewHolder>() {

    private var items: List<WeatherData> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }
    fun setItems(data: List<WeatherData>) {
        items = data
        notifyDataSetChanged()
    }

    @SuppressLint("StringFormatMatches", "NewApi")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data: WeatherData = items[position]

        ViewCompat.setTransitionName(holder.name,"secondTransitionName_$position")

        holder.name.text = data.name
        holder.temp.text = context.getString(R.string.temperature_format, (data.main?.temp)?.minus(273.15)?.toInt().toString())
        holder.speed.text = context.getString(R.string.speed_format, data.wind?.speed.toString())
        holder.description.text = context.getString(R.string.description_format, data.weather?.map { it.description.toString() })
        holder.humidity.text = context.getString(R.string.humidity_format, data.main?.humidity.toString())

        holder.itemView.setOnClickListener {

           listener.action(data)
            val extras = FragmentNavigatorExtras(holder.name  to "secondTransitionName_$position")
            view.navigate(WeatherListFragmentDirections.actionWeatherListFragmentToWeatherDaysFragment(
                    data.coord!!.lat!!.toFloat(), data.coord!!.lon!!.toFloat(),"secondTransitionName_$position", position), extras)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name by lazy {
            itemView.findViewById<AppCompatTextView>(R.id.city)
        }
        val humidity by lazy {
            itemView.findViewById<AppCompatTextView>(R.id.humidity)
        }
        val temp by lazy {
            itemView.findViewById<AppCompatTextView>(R.id.temp)
        }
        val speed by lazy {
            itemView.findViewById<AppCompatTextView>(R.id.speed)
        }

        val description by lazy {
            itemView.findViewById<AppCompatTextView>(R.id.description)
        }
    }

    interface CallbackListener {
        fun action(data: WeatherData)
    }
}


