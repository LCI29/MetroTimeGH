package ru.clementl.metrotimex.ui.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import ru.clementl.metrotimex.R
import ru.clementl.metrotimex.viewmodel.SharedViewModel
import ru.clementl.metrotimex.viewmodel.ShiftCreateViewModel

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: SharedViewModel
    lateinit var shiftCreateViewModel: ShiftCreateViewModel

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        shiftCreateViewModel = ViewModelProvider(this).get(ShiftCreateViewModel::class.java)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = host.navController

        val topLevelDestinations = setOf(R.id.calendarFragment, R.id.tonightFragment)

        // Для того, чтобы при переключении по вкладкам не появлялась UpButton
        // добавляем список top level destinations
        appBarConfiguration = AppBarConfiguration(topLevelDestinations)
        // и применяем к еще navController
        // поскольку у AppBarConfiguration отсутствует конструктор, принимающий сразу
        // и NavController, и topLevelDestinations: Set<Int>
        setupActionBarWithNavController(navController, appBarConfiguration)

        setupActionBar(navController, appBarConfiguration)
        setupBottomNavMenu(navController)
    }

    private fun setupActionBar(
        navController: NavController,
        appBarConfiguration: AppBarConfiguration
    ) {

        // This allows NavigationUI to decide what label to show in the action bar
        // By using appBarConfig, it will also determine whether to
        // show the up arrow or drawer menu icon
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    // Чтобы подключить нижние вкладки к навигации. Id вкладок в меню должны совпадать с id
    // мест назначений
    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav?.setupWithNavController(navController)
    }

    // Чтобы работала кнопка вверх
    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration)
    }

    // Создать оверфлоу меню
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.overflow_menu, menu)
        return true
    }

    // Подключение пунктов оверфлоу-меню к навигации. Id должны совпадать
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }


}