import express from 'express';
import { initializeApp } from 'firebase/app';
import { collection, doc, getDoc, getDocs, getFirestore, setDoc, deleteDoc, query, where } from 'firebase/firestore/lite';

const firebaseConfig = {
    apiKey: "AIzaSyC-laGMWj-L9FKFxeEnPfY41nexYyXbQR0",
    authDomain: "socialfit-8cf31.firebaseapp.com",
    projectId: "socialfit-8cf31",
    storageBucket: "socialfit-8cf31.appspot.com",
    messagingSenderId: "70185079098",
    appId: "1:70185079098:web:0ac785353cd04f5319c003",
    measurementId: "G-5LFR48CT88"
};

const firebase = initializeApp(firebaseConfig);
const db = getFirestore(firebase);

const usersCol = collection(db, 'user');

const validateUser = (user) => {
    try {
        if (!user.id || !user.email || !user.name || !user.password || !user.phone) {
            return false;
        }
        return user.id.length >= 4 && user.id.length <= 15 &&
            user.email.length >= 5 && user.email.length <= 30 && user.email.includes('@') &&
            user.name.length >= 3 && user.name.length <= 30 &&
            user.password.length >= 8 && user.password.length <= 30 &&
            user.phone.length >= 9 && user.phone.length <= 12;
    } catch (error) {
        console.error('Error validating user: ', error);
        return false;
    }
};
const existsUser = async (id) => {
    try {
        const data = await getDocs(usersCol);
        return data.docs.some((doc) => doc.data().id === id);
    } catch (error) {
        console.error('Error checking if user exists: ', error);
        return false;
    }
};
const existsUserEmail = async (email) => {
    try {
        const data = await getDocs(usersCol);
        return data.docs.some((doc) => doc.data().email === email);
    } catch (error) {
        console.error('Error checking if user email exists: ', error);
        return false;
    }
};

const handleResponse = (res, status, message) => {
res.status(status).send({
        status: status,
        message: message
    });
};

const app = express();
const port = 3000;

app.use(express.json());

app.get('/users', async (req, res) => {
    const users = await getDocs(usersCol);
    res.send(users.docs.map((doc) => doc.data()));
});

app.get('/user/:id', async (req, res) => {
    const id = req.params.id;
    const users = await getDocs(usersCol);
    const user = users.docs.find((doc) => doc.data().id === id);
    if (user) {
        res.send(user.data());
    } else {
        handleResponse(res, 404, 'User does not exist');
    }
});

app.get("/user/:id/trainings", async (req, res) => {
    // there is an array called treinos in the user database
    const id = req.params.id;
    const users = await getDocs(usersCol);
    const user = users.docs.find((doc) => doc.data().id === id);
    if (user) {
        res.send(user.data().treinos);
    }
});

app.get("/user/:id/:trainingId", async (req, res) => {
    const id = req.params.id;
    const trainingId = req.params.trainingId;
    const users = await getDocs(usersCol);
    const user = users.docs.find((doc) => doc.data().id === id);
    if (user) {
        const trainings = user.data().treinos;
        const training = trainings[trainingId];
        if (training) {
            res.send(training);
        } else {
            handleResponse(res, 404, 'Training does not exist');
        }
    } else {
        handleResponse(res, 404, 'User does not exist');
    }
});

app.get("/user/:id/:trainingId/days", async (req, res) => {
    // map named dias inside training, that has a array for each day of the week, those containing all the trainings for that day
    const id = req.params.id;
    const trainingId = req.params.trainingId;
    const users = await getDocs(usersCol);
    const user = users.docs.find((doc) => doc.data().id === id);
    if (user) {
        const trainings = user.data().treinos;
        const training = trainings[trainingId];
        if (training) {
            res.send(training.dias);
        } else {
            handleResponse(res, 404, 'Training does not exist');
        }
    }
});

app.get("/user/:id/:trainingId/:day", async (req, res) => {
    const id = req.params.id;
    const trainingId = req.params.trainingId;
    const day = req.params.day;
    const users = await getDocs(usersCol);
    const user = users.docs.find((doc) => doc.data().id === id);
    if (user) {
        const trainings = user.data().treinos;
        const training = trainings[trainingId];
        if (training) {
            const days = training.dias;
            const dayTrainings = days[day];
            if (dayTrainings) {
                res.send(dayTrainings);
            } else {
                handleResponse(res, 404, 'Day does not exist');
            }
        } else {
            handleResponse(res, 404, 'Training does not exist');
        }
    }
});

app.post('/user', async (req, res) => {
    const newUser = req.body;
    if (!validateUser(newUser)) {
        handleResponse(res, 400, 'Invalid input data');
    } else {
        const exists = await existsUser(newUser.id);
        const existsEmail = await existsUserEmail(newUser.email);
        if (exists) {
            handleResponse(res, 409, 'User already exists');
        } else if (existsEmail) {
            handleResponse(res, 409, 'Email already exists');
        } else {
            newUser.treinos = [];
            await setDoc(doc(db, 'user', newUser.id), newUser);
            handleResponse(res, 201, 'User created');
        }
    }
});

app.post('/user/:id/training', async (req, res) => {
    const id = req.params.id;
    const newTraining = req.body;
    const users = await getDocs(usersCol);
    const user = users.docs.find((doc) => doc.data().id === id);
    if (user) {
        const trainings = user.data().treinos;
        trainings.push(newTraining);
        await setDoc(doc(db, 'user', id), user.data());
        handleResponse(res, 201, 'Training created');
    }
});

app.post('/user/:id/:trainingId/day', async (req, res) => {
    const id = req.params.id;
    const trainingId = req.params.trainingId;
    const newDay = req.body;
    const users = await getDocs(usersCol);
    const user = users.docs.find((doc) => doc.data().id === id);
    if (user) {
        const trainings = user.data().treinos;
        const training = trainings[trainingId];
        if (training) {
            const days = training.dias;
            days.push(newDay);
            await setDoc(doc(db, 'user', id), user.data());
            handleResponse(res, 201, 'Day created');
        } else {
            handleResponse(res, 404, 'Training does not exist');
        }
    }
});

app.post('/user/:id/:trainingId/:day/exercise', async (req, res) => {
    const id = req.params.id;
    const trainingId = req.params.trainingId;
    const day = req.params.day;
    const newExercise = req.body;
    const users = await getDocs(usersCol);
    const user = users.docs.find((doc) => doc.data().id === id);
    if (user) {
        const trainings = user.data().treinos;
        const training = trainings[trainingId];
        if (training) {
            const days = training.dias;
            const dayTrainings = days[day];
            if (dayTrainings) {
                const exercises = dayTrainings.exercicios;
                exercises.push(newExercise);
                await setDoc(doc(db, 'user', id), user.data());
                handleResponse(res, 201, 'Exercise created');
            } else {
                handleResponse(res, 404, 'Day does not exist');
            }
        } else {
            handleResponse(res, 404, 'Training does not exist');
        }
    }
});

app.put('/user/:id/:trainingId', async (req, res) => {
    const id = req.params.id;
    const trainingId = req.params.trainingId;
    const updatedTraining = req.body;
    const users = await getDocs(usersCol);
    const user = users.docs.find((doc) => doc.data().id === id);
    if (user) {
        const trainings = user.data().treinos;
        const training = trainings[trainingId];
        if (training) {
            trainings[trainingId] = updatedTraining;
            await setDoc(doc(db, 'user', id), user.data());
            handleResponse(res, 200, 'Training updated');
        } else {
            handleResponse(res, 404, 'Training does not exist');
        }
    }
});

app.put('/user/:id', async (req, res) => {
    const id = req.params.id;
    const updatedUser = req.body;
    if (!validateUser(updatedUser)) {
        handleResponse(res, 400, 'Invalid input data');
    } else {
        const exists = await existsUser(id);
        if (exists) {
            await setDoc(doc(db, 'user', id), updatedUser);
            handleResponse(res, 200, 'User updated');
        } else {
            handleResponse(res, 404, 'User does not exist');
        }
    }
});

app.delete('/user/:id', async (req, res) => {
    const id = req.params.id;
    const exists = await existsUser(id);
    if (exists) {
        await deleteDoc(doc(db, 'user', id));
        const trainings = await getDocs(trainingsCol);
        trainings.forEach(async (doc) => {
            if (doc.data().usuario === id) {
                await deleteDoc(doc.ref);
                const exercises = await getDocs(exercisesCol);
                exercises.forEach(async (exercise) => {
                    if (exercise.data().treino === doc.data().id) {
                        await deleteDoc(exercise.ref);
                    }
                });
            }
        });
        handleResponse(res, 200, 'User deleted');
    } else {
        handleResponse(res, 404, 'User does not exist');
    }
});

app.use((req, res) => {
    handleResponse(res, 404, '404 Not Found');
});

app.listen(port, () => {
    console.log(`Example app listening at http://localhost:${port}`);
});
