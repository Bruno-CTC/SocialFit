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
const trainingsCol = collection(db, 'treinos');
const exercisesCol = collection(db, 'exercicios');

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

const validateTraining = (training) => {
    try {
        const validDays = ['segunda', 'terca', 'quarta', 'quinta', 'sexta', 'sabado', 'domingo'];
        if (!training.dataInicio || !training.dataTermino || !training.dia || !training.id || !training.quantExercicios || !training.usuario) {
            return false;
        }
        return training.dataInicio.length === 10 && training.dataTermino.length === 10 &&
            validDays.includes(training.dia) && training.id >= 0 &&
            training.quantExercicios > 0 && training.usuario.length >= 4 && training.usuario.length <= 15;
    } catch (error) {
        console.error('Error validating training: ', error);
        return false;
    }
};
const validateExercise = (exercise) => {
    try {
        if (!exercise.descanso || !exercise.descricao || !exercise.nome || !exercise.quant || !exercise.treino || !exercise.id) {
            return false;
        }
        return exercise.descanso >= 0 &&
            exercise.descricao.length >= 10 && exercise.descricao.length <= 100 &&
            exercise.nome.length >= 3 && exercise.nome.length <= 30 &&
            exercise.quant > 0;
    } catch (error) {
        console.error('Error validating exercise: ', error);
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
const existsExercise = async (exerciseId) => {
    const exerciseSnapshot = await getDocs(query(collection(db, 'exercicios'), where('id', '==', exerciseId)));
    return !exerciseSnapshot.empty;
};


const handleResponse = (res, status, message) => {
    res.status(status).send(message);
};

const app = express();
const port = 3000;

app.use(express.json());

app.get('/trainings', async (req, res) => {
    try {
        const trainingsSnapshot = await getDocs(trainingsCol);
        const trainings = [];
        trainingsSnapshot.forEach((doc) => {
            trainings.push({
                id: doc.id,
                ...doc.data()
            });
        });
        res.status(200).send(trainings);
    } catch (error) {
        console.error('Error retrieving trainings: ', error);
        handleResponse(res, 500, 'Internal Server Error');
    }
});

app.get('/training/:id', async (req, res) => {
    const id = req.params.id;
    try {
        const docRef = doc(trainingsCol, id.toString());
        const docSnap = await getDoc(docRef);

        if (docSnap.exists()) {
            res.status(200).send(docSnap.data());
        } else {
            handleResponse(res, 404, 'Training does not exist');
        }
    } catch (error) {
        console.error('Error retrieving training: ', error);
        handleResponse(res, 500, 'Internal Server Error');
    }
});

app.post('/training', async (req, res) => {
    const newTraining = req.body;
    try {
        const existingTrainings = await getDocs(trainingsCol);
        const isExisting = existingTrainings.docs.some(doc => doc.id === newTraining.id.toString());
        if (isExisting) {
            handleResponse(res, 409, 'Training already exists');
        } else if (!validateTraining(newTraining)) {
            handleResponse(res, 400, 'Invalid input data');
        } else {
            await setDoc(doc(trainingsCol, newTraining.id.toString()), newTraining);
            handleResponse(res, 200, 'Training added');
        }
    } catch (error) {
        console.error('Error adding training: ', error);
        handleResponse(res, 500, 'Internal Server Error');
    }
});

app.put('/training/:id', async (req, res) => {
    const id = req.params.id;
    const updatedTraining = req.body;
    try {
        const existingTrainings = await getDocs(trainingsCol);
        const isExisting = existingTrainings.docs.some(doc => doc.id === id.toString());
        if (!isExisting) {
            handleResponse(res, 404, 'Training does not exist');
        } else if (!validateTraining(updatedTraining)) {
            handleResponse(res, 400, 'Invalid input data');
        } else {
            await setDoc(doc(trainingsCol, id.toString()), updatedTraining);
            handleResponse(res, 200, 'Training updated');
        }
    } catch (error) {
        console.error('Error updating training: ', error);
        handleResponse(res, 500, 'Internal Server Error');
    }
});

app.delete('/training/:id', async (req, res) => {
    const id = req.params.id;
    try {
        const existingTrainings = await getDocs(trainingsCol);
        const isExisting = existingTrainings.docs.some(doc => doc.id === id.toString());
        if (!isExisting) {
            handleResponse(res, 404, 'Training does not exist');
        } else {
            await deleteDoc(doc(trainingsCol, id.toString()));
            const exercises = await getDocs(exercisesCol);
            exercises.forEach(async (doc) => {
                if (doc.data().treino === id.toString()) {
                    await deleteDoc(doc.ref);
                }
            });
            handleResponse(res, 200, 'Training deleted');
        }
    } catch (error) {
        console.error('Error deleting training: ', error);
        handleResponse(res, 500, 'Internal Server Error');
    }
});

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

app.post('/user', async (req, res) => {
    const newUser = req.body;
    if (!validateUser(newUser)) {
        handleResponse(res, 400, 'Invalid input data');
    } else {
        const exists = await existsUser(newUser.id);
        if (exists) {
            handleResponse(res, 409, 'User already exists');
        } else {
            const emailExists = await existsUserEmail(newUser.email);
            if (emailExists) {
                handleResponse(res, 409, 'Email already in use');
            } else {
                await setDoc(doc(db, 'user', newUser.id), newUser);
                handleResponse(res, 200, 'User added');
            }
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

app.get('/exercise/:training', async (req, res) => {
    const training = req.params.training;
    const exercisesCol = collection(db, 'exercicios');
    const querySnapshot = await getDocs(query(exercisesCol, where('treino', '==', training)));
    const exercises = [];
    querySnapshot.forEach((doc) => {
        exercises.push(doc.data());
    });
    if (exercises.length > 0) {
        res.send(exercises);
    } else {
        handleResponse(res, 404, 'Exercise does not exist');
    }
});

app.post('/exercise', async (req, res) => {
    const newExercise = req.body;
    if (!validateExercise(newExercise)) {
        handleResponse(res, 400, 'Invalid input data');
    } else {
        const exists = await existsExercise(newExercise.id);
        if (exists) {
            handleResponse(res, 409, 'Exercise already exists');
        } else {
            await setDoc(doc(db, 'exercicios', newExercise.id.toString()), newExercise);
            handleResponse(res, 200, 'Exercise added');
        }
    }
});

app.get('/exercises', async (req, res) => {
    try {
        const exercisesSnapshot = await getDocs(exercisesCol);
        const exercises = [];
        exercisesSnapshot.forEach((doc) => {
            exercises.push({
                id: doc.id,
                ...doc.data()
            });
        });
        res.status(200).send(exercises);
    } catch (error) {
        console.error('Error retrieving exercises: ', error);
        handleResponse(res, 500, 'Internal Server Error');
    }
});

app.put('/exercise/:id', async (req, res) => {
    const id = req.params.id;
    const updatedExercise = req.body;
    if (!validateExercise(updatedExercise)) {
        handleResponse(res, 400, 'Invalid input data');
    } else {
        const exists = await existsExercise(id);
        if (exists) {
            await setDoc(doc(db, 'exercicios', id), updatedExercise);
            handleResponse(res, 200, 'Exercise updated');
        } else {
            handleResponse(res, 404, 'Exercise does not exist');
        }
    }
});

app.delete('/exercise/:id', async (req, res) => {
    const id = req.params.id;
    const exists = await existsExercise(id);
    if (exists) {
        await deleteDoc(doc(db, 'exercicios', id));
        handleResponse(res, 200, 'Exercise deleted');
    } else {
        handleResponse(res, 404, 'Exercise does not exist');
    }
});

app.use((req, res) => {
    handleResponse(res, 404, '404 Not Found');
});

app.listen(port, () => {
    console.log(`Example app listening at http://localhost:${port}`);
});
