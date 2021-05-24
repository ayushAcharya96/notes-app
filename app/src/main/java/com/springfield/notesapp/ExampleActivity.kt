package com.springfield.notesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.util.*
import kotlin.collections.HashMap

class ExampleActivity : AppCompatActivity() {

    private val TAG = "ExampleActivity"

    val firestore = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)
    }

    fun createDocument(view: View) {
//        Toast.makeText(this, "createDocument", Toast.LENGTH_SHORT).show()

//        val map = HashMap<String, Any>()
//        map.put("text", "i wanna complete a little girls diary today")
//        map.put("isCompleted", false)
//        map.put("created", Timestamp(Date()))
//
//        firestore.collection("notes")
//            .add(map)
//            .addOnSuccessListener {
//                Log.d(TAG, "onSuccess: task was successful")
//                Log.d(TAG, "onSuccess: " + it.id)
//            }
//            .addOnFailureListener {
//                Log.d(TAG, "onFailure: task was unsuccessful")
//            }

        val map = HashMap<String, Any>()
        map.put("name", "Ipad mini")
        map.put("price", 799)
        map.put("isAvailable", true)

        val product = Product("Mac Book Pro", 2099.0, true)

        FirebaseFirestore.getInstance()
            .collection("products")
            .add(product)
            .addOnSuccessListener {
                Log.d(TAG, "onSuccess: Product is added successfully")
                Log.d(TAG, "onSuccess: " + it.id)
            }
            .addOnFailureListener {
                Log.e(TAG, "onFailure: $it")
            }

    }
    fun readDocument(view: View) {
//        Toast.makeText(this, "readDocument", Toast.LENGTH_SHORT).show()

//        FirebaseFirestore.getInstance()
//                .collection("products")
//                .get()
//                .addOnSuccessListener {
//                    Log.d(TAG, "onSuccess: we are getting data")
//                    val snapshotList = it.documents
//                    for ( snapshot in snapshotList) {
//                        Log.d(TAG, "onSuccess: ${snapshot.getData().toString()}")
//                    }
//
//                }
//                .addOnFailureListener {
//                    Log.e(TAG, "onFailure: $it")
//
//                }

//        FirebaseFirestore.getInstance()
//                .collection("products")
////                .whereLessThan("price", 1000)
//                .whereEqualTo("isAvailable", false)
//                .get()
//                .addOnSuccessListener {
//                    Log.d(TAG, "onSuccess: we are getting data")
//                    val snapshotList = it.documents
//                    for ( snapshot in snapshotList) {
//                        Log.d(TAG, "onSuccess: ${snapshot.getData().toString()}")
//                    }
//
//                }
//                .addOnFailureListener {
//                    Log.e(TAG, "onFailure: $it")
//
//                }

//        FirebaseFirestore.getInstance()
//                .collection("products")
//                .document("DSaOXNWqcJBlkYTvcV7a")
//                .get()
//                .addOnSuccessListener {
//                    Log.d(TAG, "onSuccess: ${it.getData()}")
//                }
//                .addOnFailureListener {
//                    Log.e(TAG, "onFailure: $it")
//                }

        FirebaseFirestore.getInstance()
                .collection("products")
                .orderBy("price")
                .get()
                .addOnSuccessListener {
                    val snapshotList = it.documents
                    for(snapshot in snapshotList) {
                        Log.d(TAG, "onSuccess: ${snapshot.getData()}")
                    }
                }
                .addOnFailureListener {
                    Log.e(TAG, "onFailure: $it")
                }

    }
    fun updateDocument(view: View) {
//        Toast.makeText(this, "updateDocument", Toast.LENGTH_SHORT).show()

        val doc = FirebaseFirestore.getInstance()
                .collection("products")
                .document("DSaOXNWqcJBlkYTvcV7a")

        val map = HashMap<String, Any>()
        map.put("availability", false)
        map.put("price", FieldValue.increment(100))
//        doc.update(map)
//                .addOnSuccessListener {
//                    Log.d(TAG, "onSuccess: updated successfully")
//                }
//                .addOnFailureListener {
//                    Log.d(TAG, "onFailure: $it")
//                }

//        doc.set(map)
//                .addOnSuccessListener {
//                    Log.d(TAG, "onSuccess: setting the doc")
//                }
//                .addOnFailureListener {
//                    Log.d(TAG, "onFailure: $it")
//                }


        doc.set(map, SetOptions.merge())
                .addOnSuccessListener {
                    Log.d(TAG, "onSuccess: setting the doc")
                }
                .addOnFailureListener {
                    Log.d(TAG, "onFailure: $it")
                }

    }
    fun deleteDocument(view: View) {
//        Toast.makeText(this, "deleteDocument", Toast.LENGTH_SHORT).show()

//        FirebaseFirestore.getInstance()
//                .collection("products")
//                .document("e5e71RKQ8zkA2G2hNToz")
//                .delete()
//                .addOnSuccessListener {
//                    Log.d(TAG, "onSuccess: we have deleted the document")
//                }
//                .addOnFailureListener {
//                    Log.d(TAG, "onFailure: $it")
//                }

        FirebaseFirestore.getInstance()
                .collection("products")
                .whereEqualTo("brand", "Apple")
                .get()
                .addOnSuccessListener {

                    val batch = FirebaseFirestore.getInstance().batch()

                    val snapshotList = it.documents
                    for(snapshot in snapshotList) {
                        batch.delete(snapshot.reference)
                    }
                    batch.commit()
                            .addOnSuccessListener {
                                Log.d(TAG, "onSuccess: Deleted all docs with brand = Apple")
                            }
                            .addOnFailureListener {
                                Log.d(TAG, "onFailure: $it")
                            }

                }
                .addOnFailureListener {

                }

    }
    fun getAllDocumentsWithRealTimeUpdates(view: View) {
//        Toast.makeText(this, "getAllDocumentsWithRealTimeUpdates", Toast.LENGTH_SHORT).show()

//        FirebaseFirestore.getInstance()
//                .collection("products")
//                .addSnapshotListener { value, error ->
//                    if (error != null) {
//                        Log.e(TAG, "onEvent: $error")
//                    }
//
//                    if (value != null) {
//                        Log.d(TAG, "onEvent: --------------------------")
//                        val snapshotList = value.documents
////                        for (snapshot in snapshotList) {
////                            Log.d(TAG, "onEvent: ${snapshot.getData()}")
////                        }
//
//                        val documentChangeList = value.getDocumentChanges()
//                        for(documentChange in documentChangeList) {
//                            Log.d(TAG, "onEvent: ${documentChange.document.data}")
//                        }
//
//                    } else {
//                        Log.e(TAG, "onEvent: query snapshot was null")
//                    }
//                }

        FirebaseFirestore.getInstance()
                .collection("products")
                .document("DSaOXNWqcJBlkYTvcV7a")
                .addSnapshotListener { documentSnapshot, e ->
                    if (e != null) {
                        Log.e(TAG, "onEvent: $e")
                        return@addSnapshotListener
                    }
                    if (documentSnapshot != null) {

                        Log.d(TAG, "onEvent: -------------------------------")
                        Log.d(TAG, "onEvent: ${documentSnapshot.getData()}")

                    } else {
                        Log.e(TAG, "onEvent: NULL")
                    }
                }

    }
    fun getAllDocuments(view: View) {
        Toast.makeText(this, "getAllDocuments", Toast.LENGTH_SHORT).show()
    }
}