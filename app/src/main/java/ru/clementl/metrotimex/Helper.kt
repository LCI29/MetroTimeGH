package ru.clementl.metrotimex

import android.content.Context
import android.util.TypedValue
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

// get edit text layout
fun getEditTextLayout(context: Context, hint: String): ConstraintLayout {
    val constraintLayout = ConstraintLayout(context)
    val layoutParams = ConstraintLayout.LayoutParams(
        ConstraintLayout.LayoutParams.MATCH_PARENT,
        ConstraintLayout.LayoutParams.WRAP_CONTENT
    )
    constraintLayout.layoutParams = layoutParams
    constraintLayout.id = View.generateViewId()

    val textInputLayout = TextInputLayout(context)
    textInputLayout.boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_OUTLINE
    layoutParams.setMargins(
        32.toDp(context),
        8.toDp(context),
        32.toDp(context),
        8.toDp(context)
    )
    textInputLayout.layoutParams = layoutParams
    textInputLayout.hint = hint
    textInputLayout.id = View.generateViewId()
    textInputLayout.tag = "textInputLayoutTag"


    val textInputEditText = TextInputEditText(context)
    textInputEditText.id = View.generateViewId()
    textInputEditText.tag = "textInputEditTextTag"

    textInputLayout.addView(textInputEditText)

    val constraintSet = ConstraintSet()
    constraintSet.clone(constraintLayout)

    constraintLayout.addView(textInputLayout)
    return constraintLayout
}


// extension method to convert pixels to dp
fun Int.toDp(context: Context):Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,this.toFloat(),context.resources.displayMetrics
).toInt()