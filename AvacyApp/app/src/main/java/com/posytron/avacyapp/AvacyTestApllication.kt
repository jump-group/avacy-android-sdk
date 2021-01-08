package com.posytron.avacyapp

import android.app.Application
import com.posytron.avacycmp.AvacyCMP

class AvacyTestApplication() : Application() {
    override fun onCreate() {
        super.onCreate()
        AvacyCMP.inizialize(
            "https://beta-shop.movibell.com/chapeau-reggio-calabria/?_mba=YXQ9b2VIMTFwLytJR2cvTytDaE9TMkczQjdsekFFRC9qQXd3WnlZMUhScFBQN2VwZWRzejlkelRZ\n" +
                    "cmx0SFR6TzF6WmNwWWJrS2tidFlkVmppeTd1aXFuR056THdiY1M0K3hYYjFnOFlLN2U4OHRZekZV\n" +
                    "L0ZPNVMrakV6UFZPRy90ZGM="
        )
    }
}