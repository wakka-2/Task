package com.sure.majlestech.models

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import java.io.Serializable

data class FragmentModel (val fragment: Fragment,val fragmentManager: FragmentManager): Serializable