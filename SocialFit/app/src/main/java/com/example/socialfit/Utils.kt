package com.example.socialfit

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.socialfit.data.ExerciseData
import com.example.socialfit.data.TrainingData
import com.example.socialfit.data.UserData
import org.json.JSONArray
import org.json.JSONObject
import java.nio.charset.Charset

class Utils {
    companion object {
        const val ROOT_URL = "http://192.168.15.102:3000"
        fun showDialog(context: Context, title: String, message: String?) {
            AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show()
        }

        fun saveVariable(context: Context, name: String, value: String) {
            val sharedPref = context.getSharedPreferences("SocialFit", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString(name, value)
                apply()
            }
        }

        fun getVariable(context: Context, name: String): String? {
            val sharedPref = context.getSharedPreferences("SocialFit", Context.MODE_PRIVATE)
            return sharedPref.getString(name, null)
        }

        fun removeVariable(context: Context, name: String) {
            val sharedPref = context.getSharedPreferences("SocialFit", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                remove(name)
                apply()
            }
        }

        fun currentDay(): String {
            val calendar = java.util.Calendar.getInstance()
            val days = arrayOf("Domingo", "Segunda", "Terca", "Quarta", "Quinta", "Sexta", "Sabado")
            return days[calendar.get(java.util.Calendar.DAY_OF_WEEK) - 1]
        }

        fun getUserData(context: Context, username: String, callback: (UserData) -> Unit, error: (String) -> Unit) {
            val queue = Volley.newRequestQueue(context)
            val request = JsonObjectRequest(ROOT_URL + "/user/" + username, { response: JSONObject ->
                try {
                    val userData = UserData()
                    userData.username = response.getString("id")
                    userData.name = response.getString("name")
                    userData.email = response.getString("email")
                    userData.password = response.getString("password")
                    userData.phone = response.getString("phone")
                    userData.treinoSelecionado = response.getInt("treinoAtual")
                    callback(userData)
                } catch (e: Exception) {
                    error(e.toString())
                }
            }, { error: VolleyError ->
                val UTF_8: Charset = Charset.forName("UTF-8")
                val body = String(error.networkResponse.data, UTF_8)
                error(body)
            })
            queue.add(request)
        }
        fun getUserTrainings(context: Context, username: String, callback: (ArrayList<TrainingData>) -> Unit, error: (String) -> Unit) {
            val queue = Volley.newRequestQueue(context)
            val request = JsonArrayRequest(ROOT_URL + "/user/" + username + "/trainings", { response: JSONArray? ->
                try {
                    val trainings = ArrayList<TrainingData>()
                    for (i in 0 until response!!.length()) {
                        val trainingJson = response.getJSONObject(i)
                        val training = TrainingData()
                        training.description = trainingJson.getString("descricao")
                        training.name = trainingJson.getString("nome")
                        training.id = i
                        trainings.add(training)
                    }
                    callback(trainings)
                } catch (e: Exception) {
                    error(e.toString())
                }
            }, { error: VolleyError ->
                val UTF_8: Charset = Charset.forName("UTF-8")
                val body = String(error.networkResponse.data, UTF_8)
                error(body)
            })
            queue.add(request)
        }
        fun getTrainingExercises(context: Context, username: String, trainingId: Int, day: String, callback: (ArrayList<ExerciseData>) -> Unit, error: (String) -> Unit) {
            val queue = Volley.newRequestQueue(context)
            val request = JsonArrayRequest(ROOT_URL + "/user/" + username + "/" + trainingId + "/" + day, { response: JSONArray? ->
                try {
                    val exercises = ArrayList<ExerciseData>()
                    for (i in 0 until response!!.length()) {
                        val exerciseJson = response.getJSONObject(i)
                        val exercise = ExerciseData()
                        try {
                            exercise.name = exerciseJson.getString("nome")
                            exercise.description = exerciseJson.getString("descricao")
                            exercise.repetitions = exerciseJson.getInt("repeticoes")
                            exercise.series = exerciseJson.getInt("series")
                            exercise.rest = exerciseJson.getInt("descanso")
                            exercise.id = i
                        } catch (e: Exception) {
                            break;
                        }
                        exercises.add(exercise)
                    }
                    callback(exercises)
                } catch (e: Exception) {
                    error(e.toString())
                }
            }, { error: VolleyError ->
                val UTF_8: Charset = Charset.forName("UTF-8")
                val body = String(error.networkResponse.data, UTF_8)
                error(body)
            })
            queue.add(request)
        }
        fun getExerciseData(context: Context, username: String, trainingId: Int, day: String, exerciseId: Int, callback: (ExerciseData) -> Unit, error: (String) -> Unit) {
            val queue = Volley.newRequestQueue(context)
            val request = JsonObjectRequest(ROOT_URL + "/user/" + username + "/" + trainingId + "/" + day + "/" + exerciseId, { response: JSONObject ->
                try {
                    val exercise = ExerciseData()
                    exercise.name = response.getString("nome")
                    exercise.description = response.getString("descricao")
                    exercise.repetitions = response.getInt("repeticoes")
                    exercise.series = response.getInt("series")
                    exercise.rest = response.getInt("descanso")
                    callback(exercise)
                } catch (e: Exception) {
                    error(e.toString())
                }
            }, { error: VolleyError ->
                val UTF_8: Charset = Charset.forName("UTF-8")
                val body = String(error.networkResponse.data, UTF_8)
                error(body)
            })
            queue.add(request)
        }
        fun postUserData(context: Context, userData: UserData, callback: (String) -> Unit, error : (String) -> Unit) {
            val queue = Volley.newRequestQueue(context)
            val params = JSONObject()
            params.put("id", userData.username)
            params.put("name", userData.name)
            params.put("email", userData.email)
            params.put("password", userData.password)
            params.put("phone", userData.phone)
            val request = object : StringRequest(
                Method.POST, "$ROOT_URL/user",
                Response.Listener { response ->
                    callback(response)
                },
                Response.ErrorListener { error ->
                    error(String(error.networkResponse.data, Charset.forName("UTF-8")))
                }) {
                override fun getBody(): ByteArray {
                    return params.toString().toByteArray()
                }
                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
            queue.add(request)
        }
        fun postTrainingData(context: Context, username: String, trainingData: TrainingData, callback: (String) -> Unit, error : (String) -> Unit) {
            val queue = Volley.newRequestQueue(context)
            val params = JSONObject()
            params.put("nome", trainingData.name)
            params.put("descricao", trainingData.description)
            val request = object : StringRequest(
                Method.POST, "$ROOT_URL/user/$username/training",
                Response.Listener { response ->
                    callback(response)
                },
                Response.ErrorListener { error ->
                    error(error.toString())
                }) {
                override fun getBody(): ByteArray {
                    return params.toString().toByteArray()
                }
                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
            queue.add(request)
        }
        fun postExerciseData(context: Context, username: String, trainingId: Int, day: String, exerciseData: ExerciseData, callback: (String) -> Unit, error : (String) -> Unit) {
            val queue = Volley.newRequestQueue(context)
            val params = JSONObject()
            params.put("nome", exerciseData.name)
            params.put("descricao", exerciseData.description)
            params.put("repeticoes", exerciseData.repetitions)
            params.put("series", exerciseData.series)
            params.put("descanso", exerciseData.rest)
            val request = object : StringRequest(
                Method.POST, "$ROOT_URL/user/$username/$trainingId/$day",
                Response.Listener { response ->
                    callback(response)
                },
                Response.ErrorListener { error ->
                    error(error.toString())
                }) {
                override fun getBody(): ByteArray {
                    return params.toString().toByteArray()
                }
                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
            queue.add(request)
        }
        fun putUserData(context: Context, userData: UserData, callback: (String) -> Unit, error : (String) -> Unit) {
            val queue = Volley.newRequestQueue(context)
            val params = JSONObject()
            params.put("id", userData.username)
            params.put("name", userData.name)
            params.put("email", userData.email)
            params.put("password", userData.password)
            params.put("phone", userData.phone)
            params.put("treinoAtual", userData.treinoSelecionado)
            val request = object : StringRequest(
                Method.PUT, "$ROOT_URL/user/${userData.username}",
                Response.Listener { response ->
                    callback(response)
                },
                Response.ErrorListener { error ->
                    error(String(error.networkResponse.data, Charset.forName("UTF-8")))
                }) {
                override fun getBody(): ByteArray {
                    return params.toString().toByteArray()
                }
                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
            queue.add(request)
        }
        fun putTrainingData(context: Context, username: String, trainingId: Int, trainingData: TrainingData, callback: (String) -> Unit, error : (String) -> Unit) {
            val queue = Volley.newRequestQueue(context)
            val params = JSONObject()
            params.put("nome", trainingData.name)
            params.put("descricao", trainingData.description)
            val request = object : StringRequest(
                Method.PUT, "$ROOT_URL/user/$username/$trainingId",
                Response.Listener { response ->
                    callback(response)
                },
                Response.ErrorListener { error ->
                    error(error.toString())
                }) {
                override fun getBody(): ByteArray {
                    return params.toString().toByteArray()
                }
            }
            queue.add(request)
        }
        fun putExerciseData(context: Context, username: String, trainingId: Int, day: String, exerciseId: Int, exerciseData: ExerciseData, callback: (String) -> Unit, error : (String) -> Unit) {
            val queue = Volley.newRequestQueue(context)
            val params = JSONObject()
            params.put("nome", exerciseData.name)
            params.put("descricao", exerciseData.description)
            params.put("repeticoes", exerciseData.repetitions)
            params.put("series", exerciseData.series)
            params.put("descanso", exerciseData.rest)
            val request = object : StringRequest(
                Method.PUT, "$ROOT_URL/user/$username/$trainingId/$day/$exerciseId",
                Response.Listener { response ->
                    callback(response)
                },
                Response.ErrorListener { error ->
                    error(error.toString())
                }) {
                override fun getBody(): ByteArray {
                    return params.toString().toByteArray()
                }
            }
            queue.add(request)
        }
        fun deleteUserData(context: Context, username: String, callback: (String) -> Unit, error : (String) -> Unit) {
            val queue = Volley.newRequestQueue(context)
            val request = object : StringRequest(
                Method.DELETE, "$ROOT_URL/user/$username",
                Response.Listener { response ->
                    callback(response)
                },
                Response.ErrorListener { error ->
                    error(error.toString())
                }) {
            }
            queue.add(request)
        }
        fun deleteTraining(context: Context, username: String, trainingId: Int, callback: (String) -> Unit, error : (String) -> Unit) {
            val queue = Volley.newRequestQueue(context)
            val request = object : StringRequest(
                Method.DELETE, "$ROOT_URL/user/$username/$trainingId",
                Response.Listener { response ->
                    callback(response)
                },
                Response.ErrorListener { error ->
                    error(error.toString())
                }) {
            }
            queue.add(request)
        }
        fun deleteExercise(context: Context, username: String, trainingId: Int, day: String, exerciseId: Int, callback: (String) -> Unit, error : (String) -> Unit) {
            val queue = Volley.newRequestQueue(context)
            val request = object : StringRequest(
                Method.DELETE, "$ROOT_URL/user/$username/$trainingId/$day/$exerciseId",
                Response.Listener { response ->
                    callback(response)
                },
                Response.ErrorListener { error ->
                    error(error.toString())
                }) {
            }
            queue.add(request)
        }

        // others
        fun getPremadeTrainings(context: Context, callback: (ArrayList<TrainingData>) -> Unit, error: (String) -> Unit) {
            val queue = Volley.newRequestQueue(context)
            val request = JsonArrayRequest(ROOT_URL + "/treinos-pre-preparados",
                { response: JSONArray? ->
                    try {
                        val trainings = ArrayList<TrainingData>()

                        for (i in 0 until response!!.length()) {
                            val trainingJson = response.getJSONObject(i)
                            val training = TrainingData().apply {
                                description = trainingJson.getString("descricao")
                                name = trainingJson.getString("nome")
                                id = i
                            }
                            trainings.add(training)

                            val exercisesJson = trainingJson.getJSONObject("dias")
                            val daysNames = arrayOf("Segunda", "Terca", "Quarta", "Quinta", "Sexta", "Sabado", "Domingo")

                            for (j in 0 until 5) {
                                val dayExercisesJson = exercisesJson.getJSONArray(daysNames[j])
                                val exercises = ArrayList<ExerciseData>()

                                for (k in 0 until dayExercisesJson.length()) {
                                    val exerciseJson = dayExercisesJson.getJSONObject(k)
                                    val exercise = ExerciseData().apply {
                                        name = exerciseJson.getString("nome")
                                        description = exerciseJson.getString("descricao")
                                        repetitions = exerciseJson.getInt("repeticoes")
                                        series = exerciseJson.getInt("series")
                                        rest = exerciseJson.getInt("descanso")
                                        id = k
                                    }
                                    exercises.add(exercise)
                                }

                                training.days[training.name] = exercises
                            }
                        }
                        callback(trainings)
                    } catch (e: Exception) {
                        error(e.toString())
                    }
                },
                { error: VolleyError ->
                    val UTF_8: Charset = Charset.forName("UTF-8")
                    val body = String(error.networkResponse.data, UTF_8)
                    error(body)
                })

            queue.add(request)
        }
        fun getPremadeTraining(context: Context, trainingId: Int, callback: (TrainingData) -> Unit, error: (String) -> Unit) {
            val queue = Volley.newRequestQueue(context)
            val request = JsonObjectRequest(ROOT_URL + "/treinos-pre-preparados/$trainingId",
                { response: JSONObject? ->
                    try {
                        val training = TrainingData().apply {
                            description = response!!.getString("descricao")
                            name = response.getString("nome")
                            id = trainingId
                        }

                        val exercisesJson = response!!.getJSONObject("dias")
                        val daysNames = arrayOf("Segunda", "Terca", "Quarta", "Quinta", "Sexta", "Sabado", "Domingo")

                        for (j in 0 until 5) {
                            val dayExercisesJson = exercisesJson.getJSONArray(daysNames[j])
                            val exercises = ArrayList<ExerciseData>()

                            for (k in 0 until dayExercisesJson.length()) {
                                val exerciseJson = dayExercisesJson.getJSONObject(k)
                                val exercise = ExerciseData().apply {
                                    name = exerciseJson.getString("nome")
                                    description = exerciseJson.getString("descricao")
                                    repetitions = exerciseJson.getInt("repeticoes")
                                    series = exerciseJson.getInt("series")
                                    rest = exerciseJson.getInt("descanso")
                                    id = k
                                }
                                exercises.add(exercise)
                            }

                            training.days[training.name] = exercises
                        }

                        callback(training)
                    } catch (e: Exception) {
                        error(e.toString())
                    }
                },
                { error: VolleyError ->
                    val UTF_8: Charset = Charset.forName("UTF-8")
                    val body = String(error.networkResponse.data, UTF_8)
                    error(body)
                })

            queue.add(request)
        }
        fun postPremadeTraining(context: Context, username: String, trainingId: Int, callback: (String) -> Unit, error : (String) -> Unit) {
            // make a post request to /user/:userId/add-premade/:premadeId
            val queue = Volley.newRequestQueue(context)
            val request = object : StringRequest(
                Method.POST, "$ROOT_URL/user/$username/add-premade/$trainingId",
                Response.Listener { response ->
                    callback(response)
                },
                Response.ErrorListener { error ->
                    error(error.toString())
                }) {
            }
            queue.add(request)
        }

    }
}