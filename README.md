# EventFlow - Etkinlik Yönetim Uygulaması

EventFlow, organizasyonel etkinliklerin (nişan, söz, workshop vb.) planlanmasını ve yönetilmesini kolaylaştırmak için geliştirilmiş bir mobil uygulamadır.
Küçük işletmelerin ve bireylerin etkinliklerini etkili bir şekilde organize etmelerine olanak tanır.
Kullanıcılar etkinlik detaylarını (başlık, açıklama, tarih, saat, hzimet, müşteri) ekleyebilir, etkinliklerini düzenleyebilir veya liste halinde görüntüleyebilir.
Tüm etkinliklerini görüntüleyebilir veya takvimden seçtiği bir günün etkinliklerini düzenleyebilir. <br/>
Firebase Firestore tabanlı altyapısı sayesinde gerçek zamanlı veri yönetimini destekler ve kullanıcı yetkilendirme işlemlerini güvenli bir şekilde gerçekleştirir.

## Öne Çıkan Özellikler

- Kullanıcı girişi ve kayıt işlemleri (Firebase Authentication)
- Etkinlik ekleme, düzenleme ve silme işlevleri
- Etkinliklerin tarih, saat gibi detaylarla listelenmesi
- Rezervasyonların tümünün listelenmesi ve arama yapılması

## Geliştirilmesi Planlanan Özellikler

- Account yönetimi
- Tarihi yaklaşan etkinliklerin hatırlatılması
- Konum özelliği
- logout

## Mimari

Proje mimarisi olarak **MVVM (Model-View-ViewModel)** kullanılmıştır. <br/>

MVVM, kodu modüler bir şekilde düzenlemeye olanak tanır. Model, View ve ViewModel'in ayrı tutulması, kodu daha düzenli ve sürdürülebilir hale getirir. <br/>
**Model:** Veri tabanı, ağ istekleri veya local depolama ile ilgili işlemleri içerir.<br/>
**View:** Aktiviteler, fragmentler aracılığı ile görsel bileşenleri içerir.<br/>
**ViewModel:** Model ve View arasında bir bağlantı sağlar. Kullanıcı arayüzüyle ilgili işlemleri içerir ve View'i doğrudan etkilemeden, Model'den gelen verilere erişim sağlar.<br/>

## Kullanılan Teknolojiler

- Kotlin
- MVVM
- Hilt
- Coroutines
- Firebase Authentication
- Firebase FireStore
- LiveData
- UseCase
- Navigation Components
- Data Binding
- Repository
- Fragment

## Ekran Görüntüleri

<img src="screenshots/img_login.png" alt="görsel 1" width="360" height="720"/> <img src="screenshots/img_signin.png" alt="görsel 2" width="360" height="720"/> <img src="screenshots/img_event.png" alt="görsel 3" width="360" height="720"/> 
<img src="screenshots/img_event_detail.png" alt="görsel 1" width="360" height="720"/> <img src="screenshots/img_services.png" alt="görsel 2" width="360" height="720"/> <img src="screenshots/img_service_detail.png" alt="görsel 3" width="360" height="720"/> 
<img src="screenshots/img_reservation.png" alt="görsel 1" width="360" height="720"/>
