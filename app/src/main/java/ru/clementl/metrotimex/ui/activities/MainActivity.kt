package ru.clementl.metrotimex.ui.activities

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import androidx.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*
import ru.clementl.metrotimex.MetroTimeApplication
import ru.clementl.metrotimex.R
import ru.clementl.metrotimex.model.data.MachinistStatus
import ru.clementl.metrotimex.ui.fragments.machinist
import ru.clementl.metrotimex.ui.fragments.ratePerHour
import ru.clementl.metrotimex.utils.logd
import ru.clementl.metrotimex.viewmodel.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var _navController: NavController
    val navController: NavController
        get() = _navController

    lateinit var statuses: LiveData<List<MachinistStatus>>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        statuses = (application as MetroTimeApplication).machinistStatusRepository.getAllAsLiveData()
        statuses.observe(this) {
            logd("""
                statuses MA = ${statuses.value}
            """.trimIndent())
        }

//        shiftCreateViewModel = ViewModelProvider(this).get(ShiftCreateViewModel::class.java)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        _navController = host.navController

        val topLevelDestinations =
            setOf(R.id.calendarFragment, R.id.tonightFragment, R.id.normaFragment)

        // Для того, чтобы при переключении по вкладкам не появлялась UpButton
        // добавляем список top level destinations
        appBarConfiguration = AppBarConfiguration(topLevelDestinations)
        // и применяем к еще navController
        // поскольку у AppBarConfiguration отсутствует конструктор, принимающий сразу
        // и NavController, и topLevelDestinations: Set<Int>
        setupActionBarWithNavController(_navController, appBarConfiguration)

        setupActionBar(_navController, appBarConfiguration)
        setupBottomNavMenu(_navController)

        toolbar.overflowIcon?.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)

        val machinistStatusRepository =
            (application as MetroTimeApplication).machinistStatusRepository
        CoroutineScope(Job() + Dispatchers.IO).launch {
            if (machinistStatusRepository.getAll().isEmpty()) {
                val prefs = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
                machinistStatusRepository.insert(MachinistStatus.create(
                    prefs.machinist(),
                    ratePerHour = prefs.ratePerHour()
                ))
            }
        }

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
        menuInflater.inflate(R.menu.null_menu, menu)
        return true
    }


}