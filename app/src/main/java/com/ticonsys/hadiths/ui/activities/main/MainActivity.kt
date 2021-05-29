package com.ticonsys.hadiths.ui.activities.main


import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.ticonsys.hadiths.R
import com.ticonsys.hadiths.databinding.ActivityMainBinding
import com.ticonsys.hadiths.ui.activities.base.BaseActivity
import com.zxdmjr.material_utils.extensions.findNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val appBarConfiguration by lazy {
        AppBarConfiguration(
            setOf(
                R.id.booksFragment,
            ), null
        )
    }

    private val viewModel by viewModels<MainViewModel>()

    private val navController by lazy {
        findNavController(binding.navHostMainFragment.id)
    }

    override fun initializeViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavController()
    }

    private fun setupNavController() {
        setSupportActionBar(binding.toolbar)
        NavigationUI.setupWithNavController(
            binding.toolbar,
            navController,
            appBarConfiguration
        )

    }


}