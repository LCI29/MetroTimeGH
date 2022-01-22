package ru.clementl.metrotimex.screens.info

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import ru.clementl.metrotimex.BuildConfig
import ru.clementl.metrotimex.R

class InfoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return requireActivity().layoutInflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showVersion(view)
        bindMetroTest(view)
        bindArithmetics(view)
    }

    private fun showVersion(view: View) {
        view.findViewById<TextView>(R.id.text_version).text =
            resources.getString(R.string.version_text, BuildConfig.VERSION_NAME)
    }

    private fun bindMetroTest(view: View) {
        view.findViewById<ImageView>(R.id.metrotest_img).setOnClickListener {
            val ref = getString(R.string.metrotest_ref)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(ref))
            startActivity(intent)
        }
    }

    private fun bindArithmetics(view: View) {
        view.findViewById<ImageView>(R.id.arithmetic_img).setOnClickListener {
            val ref = getString(R.string.arithmetics_ref)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(ref))
            startActivity(intent)
        }
    }
}

