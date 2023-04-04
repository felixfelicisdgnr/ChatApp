package com.doganur.mychatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    /*
 Yöntemi, klavye açıldığında ekranda yer değiştirme işlemini kontrol etmek için kullanılır.
 Bu yöntem, klavye açıldığında ekranı otomatik olarak yukarı kaydırmak yerine,
 ekranın içeriğini klavyenin üzerinde kalmaya devam ettirerek görüntüyü düzenlemeyi sağlar.
 Bu işlem, klavye ile etkileşimli bir kullanıcı arayüzü tasarlamak istediğinizde kullanışlıdır.
 Bu yöntemi kullanarak, kullanıcının klavye girişini görmesine izin vererek, uygulamanızda veri girişi yapmasını kolaylaştırabilirsiniz.
         */

    }
}