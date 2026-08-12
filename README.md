# CalculatorVault — Android Kotlin

تطبيق Android يعمل كآلة حاسبة طبيعية، وعند إدخال الرمز السري يفتح مخزناً لإدارة الملفات والتطبيقات المخفية.

## المواصفات (مضبوطة لتوافق مضمون)

| المكوّن | الإصدار | السبب |
|---|---|---|
| Android Gradle Plugin (AGP) | 8.2.2 | متوافق مع Gradle 8.2 ومُختبَر مع JDK 17 |
| Gradle Wrapper | 8.2 | الحد الأدنى لـ AGP 8.2.x |
| Kotlin | 1.9.22 | متوافق مع AGP 8.2.2 |
| JDK | 17 (Temurin) | الحد الأدنى الذي يطلبه AGP 8 |
| compileSdk | 34 | Android 14 |
| minSdk | 24 | Android 7.0+ |
| Material Components | 1.11.0 | ثبات مع compileSdk 34 |

تم التحقق من التطابق عبر التوثيق الرسمي لـ Android Developers.

## ما الذي يفعله التطبيق

- **الواجهة الخارجية:** آلة حاسبة عاملة بـ `exp4j` (تقييم آمن للتعبيرات).
- **الرمز السري الافتراضي:** أدخل `1234=` ثم اضغط `=` ليفتح Vault.
- **PIN الافتراضي:** `1234`.
- **في Vault:**
  - 📁 Hide/View Files (يدير ملفات SAF داخل مستندات).
  - 📦 Mark Hidden Apps (يضع قائمة التطبيقات التي تعتبر "مخفية"، أنت من يقرر لاحقاً كيف تتعامل معها).
  - ⚙️ Settings (تغيير PIN، إعادة الضبط، حذف بيانات Vault).
- تشفير PIN عبر `EncryptedSharedPreferences` باستخدام Master Key AES-256 GCM.

## الاستخدام

### الخيار ١: افتح في Android Studio (على الكمبيوتر)
1. افتح Android Studio.
2. File → Open → اختر مجلد `CalculatorVault` المستخرج.
3. Sync تلقائياً، ثم Run ▶️ على جهازك.

### الخيار ٢: بناء APK تلقائياً على GitHub (من الجوال)
1. ارفع المجلد كاملاً إلى مستودع GitHub جديد:
   - اضغط "Add file" لمجلد `.github` بالكامل (سيُنشئ المجلد تلقائياً).
   - كرّر لكل: `app/`, `gradle/`, ملفات الجذر.
   - **ملاحظة:** قد يحتاج رفع ~20 ملف فردياً — استخدم Desktop mode في Chrome لتسهيل اللصق.
2. اذهب لتبويب **Actions** ← اضغط **Build APK** ← **Run workflow**.
3. انتظر 8-15 دقيقة، ثم انزل لـ **Artifacts** وحمّل `CalculatorVault-Debug.zip`.
4. فك الضغط = تحصل على `app-debug.apk` الجاهز للتثبيت.

### الخيار ٣: بناء AAB موقّع للنشر على Google Play
استخدم workflow `Build Signed AAB for Google Play` بعد إضافة الـ Secrets الأربعة:
- `KEYSTORE_BASE64`
- `KEYSTORE_PASSWORD`
- `KEY_ALIAS`
- `KEY_PASSWORD`

## المصادر المفتوحة
- [Android Gradle plugin compatibility](https://developer.android.com/build/releases/gradle-plugin)
- [Gradle compatibility matrix](https://docs.gradle.org/current/userguide/compatibility.html)
