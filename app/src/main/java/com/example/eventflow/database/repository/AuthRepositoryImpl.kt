package com.example.eventflow.database.repository

import com.example.eventflow.database.utils.await
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : AuthRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            return Resource.Error(e)
        }
    }

    override suspend fun signUp(
        name: String,
        email: String,
        password: String,
    ): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result?.user?.updateProfile(
                UserProfileChangeRequest.Builder().setDisplayName(name).build()
            )?.await()
            Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            return Resource.Error(e)
        }
    }

    override suspend fun updateProfile(name: String): Resource<Boolean> {
        val user = firebaseAuth.currentUser // FirebaseAuth üzerinden currentUser alınıyor
        return try {
            user?.let {
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(name) // Kullanıcı adı güncelleniyor
                    .build()

                it.updateProfile(profileUpdates).await() // Asenkron işlem için `await` kullanılıyor
                Resource.Success(true) // Güncelleme başarılı ise true döndür
            } ?: Resource.Error(Exception("Kullanıcı bulunamadı")) // Kullanıcı yoksa hata döndür
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e) // Hata durumunda hatayı döndür
        }
    }

    override suspend fun changePassword(
        currentPassword: String,
        password: String,
    ): Resource<Boolean> {
        return try {
            val user = currentUser
            val email = currentUser?.email
            if (user == null || email.isNullOrEmpty()) {
                Resource.Error(Exception("Kullanıcı bulunamadı"))
            } else {
                val credential = EmailAuthProvider.getCredential(email, currentPassword)

                user.reauthenticate(credential).await()
                user.updatePassword(password).await()
                Resource.Success(true)
            }
        } catch (_: FirebaseAuthInvalidUserException) {
            Resource.Error(Exception("Hesap askıya alındı ya da silindi."))
        } catch (_: FirebaseAuthInvalidCredentialsException) {
            Resource.Error(Exception("Girilen mevcut şifre hatalı"))
        } catch (_: FirebaseAuthWeakPasswordException) {
            Resource.Error(Exception("Zayıf şifre"))
        } catch (_: FirebaseAuthInvalidUserException) {
            Resource.Error(Exception("Kullanıcı bulunamadı"))
        } catch (e: Exception) {
            Resource.Error(Exception("Hata Oluştu: ${e.message}"))
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}