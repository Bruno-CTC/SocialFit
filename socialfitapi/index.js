import express from 'express';
import { initializeApp } from 'firebase/app';
import { collection, doc, getDoc, getDocs, getFirestore, setDoc, deleteDoc, query, where, updateDoc } from 'firebase/firestore/lite';

const firebaseConfig = {
    apiKey: "AIzaSyC-laGMWj-L9FKFxeEnPfY41nexYyXbQR0",
    authDomain: "socialfit-8cf31.firebaseapp.com",
    projectId: "socialfit-8cf31",
    storageBucket: "socialfit-8cf31.appspot.com",
    messagingSenderId: "70185079098",
    appId: "1:70185079098:web:0ac785353cd04f5319c003",
    measurementId: "G-5LFR48CT88"
};

const treinoIniciante = {
    descricao: "Treino Iniciante - Primeira Sequência",
    nome: "Treino Iniciante",
    dias: {
        "Segunda": [
            {
                descricao: "Agachamento Livre",
                nome: "Agachamento Livre",
                series: 3,
                repeticoes: 10,
                descanso: 1
            },
            {
                descricao: "Supino Inclinado com Barra ou Halteres",
                nome: "Supino Inclinado",
                series: 3,
                repeticoes: 10,
                descanso: 1
            },
            {
                descricao: "Leg Press",
                nome: "Leg Press",
                series: 3,
                repeticoes: 10,
                descanso: 1
            },
            {
                descricao: "Pull-Ups (Barra Fixa) ou Pulldowns",
                nome: "Pull-Ups / Pulldowns",
                series: 3,
                repeticoes: 8,
                descanso: 1
            },
            {
                descricao: "Cadeira Extensora",
                nome: "Cadeira Extensora",
                series: 3,
                repeticoes: 12,
                descanso: 1
            }
        ],
        "Terça": [
            {
                descricao: "Deadlift",
                nome: "Deadlift",
                series: 3,
                repeticoes: 8,
                descanso: 2
            },
            {
                descricao: "Supino Reto com Barra ou Halteres",
                nome: "Supino Reto",
                series: 3,
                repeticoes: 10,
                descanso: 1
            },
            {
                descricao: "Cadeira Flexora deitado",
                nome: "Cadeira Flexora",
                series: 3,
                repeticoes: 10,
                descanso: 1
            },
            {
                descricao: "Remada com Barra T",
                nome: "Remada com Barra T",
                series: 3,
                repeticoes: 8,
                descanso: 1
            },
            {
                descricao: "Elevação de Panturrilha em Máquina",
                nome: "Elevação de Panturrilha",
                series: 3,
                repeticoes: 15,
                descanso: 1
            }
        ],
        "Quarta": [
            {
                descricao: "Agachamento Frontal",
                nome: "Agachamento Frontal",
                series: 3,
                repeticoes: 8,
                descanso: 2
            },
            {
                descricao: "Supino Declinado com Barra ou Halteres",
                nome: "Supino Declinado",
                series: 3,
                repeticoes: 10,
                descanso: 1
            },
            {
                descricao: "Afundo",
                nome: "Afundo",
                series: 3,
                repeticoes: 10,
                descanso: 1
            },
            {
                descricao: "Pull-Ups (Barra Fixa) ou Pulldowns",
                nome: "Pull-Ups / Pulldowns",
                series: 3,
                repeticoes: 8,
                descanso: 1
            },
            {
                descricao: "Elevação de Panturrilha Sentado",
                nome: "Elevação de Panturrilha Sentado",
                series: 3,
                repeticoes: 15,
                descanso: 1
            }
        ]
    }
};
treinoIniciante.dias["Quinta"] = treinoIniciante.dias["Segunda"];
treinoIniciante.dias["Sexta"] = treinoIniciante.dias["Terca"];
const treinoAvancado = {
    descricao: "Treino Avançado - Primeira Sequência",
    nome: "Treino Avançado",
    dias: {
        "Segunda":  [
            {
                descricao: "Supino Reto",
                nome: "Supino Reto",
                series: 4,
                repeticoes: 8,
                descanso: 1
            },
            {
                descricao: "Supino Inclinado",
                nome: "Supino Inclinado",
                series: 3,
                repeticoes: 8,
                descanso: 1
            },
            {
                descricao: "Crucifixo (Flies)",
                nome: "Crucifixo",
                series: 3,
                repeticoes: 10,
                descanso: 1
            },
            {
                descricao: "Paralelas (Dips)",
                nome: "Paralelas",
                series: 3,
                repeticoes: 10,
                descanso: 1
            },
            {
                descricao: "Tríceps Testa (Skull Crushers)",
                nome: "Tríceps Testa",
                series: 4,
                repeticoes: 8,
                descanso: 1
            },
            {
                descricao: "Tríceps Corda (Cable Pushdown)",
                nome: "Tríceps Corda",
                series: 3,
                repeticoes: 10,
                descanso: 1
            }
        ],
        "Terca": [
            {
                descricao: "Puxada na Frente (Pull-Ups ou Lat Pulldown)",
                nome: "Puxada na Frente",
                series: 4,
                repeticoes: 8,
                descanso: 1
            },
            {
                descricao: "Barra Fixa (Chin-Ups)",
                nome: "Barra Fixa",
                series: 3,
                repeticoes: 8,
                descanso: 1
            },
            {
                descricao: "Pulldown com Pegada Aberta (Wide-Grip Pulldown)",
                nome: "Pulldown Pegada Aberta",
                series: 3,
                repeticoes: 10,
                descanso: 1
            },
            {
                descricao: "Remada Curvada",
                nome: "Remada Curvada",
                series: 4,
                repeticoes: 8,
                descanso: 1
            },
            {
                descricao: "Rosca Direta (Barbell Curl)",
                nome: "Rosca Direta",
                series: 3,
                repeticoes: 8,
                descanso: 1
            },
            {
                descricao: "Rosca Alternada (Alternating Dumbbell Curl)",
                nome: "Rosca Alternada",
                series: 3,
                repeticoes: 10,
                descanso: 1
            }
        ],
        "Quarta": [
            {
                descricao: "Agachamento Livre (Squat)",
                nome: "Agachamento Livre",
                series: 4,
                repeticoes: 8,
                descanso: 1
            },
            {
                descricao: "Leg Press",
                nome: "Leg Press",
                series: 3,
                repeticoes: 10,
                descanso: 1
            },
            {
                descricao: "Extensora de Pernas",
                nome: "Extensora de Pernas",
                series: 3,
                repeticoes: 10,
                descanso: 1
            },
            {
                descricao: "Flexora de Pernas",
                nome: "Flexora de Pernas",
                series: 3,
                repeticoes: 10,
                descanso: 1
            },
            {
                descricao: "Panturrilha em Pé (Calf Raises)",
                nome: "Panturrilha em Pé",
                series: 4,
                repeticoes: 12,
                descanso: 1
            }
        ],
        "Quinta": [
            {
                descricao: "Desenvolvimento Militar",
                nome: "Desenvolvimento Militar",
                series: 4,
                repeticoes: 8,
                descanso: 1
            },
            {
                descricao: "Elevação Lateral",
                nome: "Elevação Lateral",
                series: 3,
                repeticoes: 10,
                descanso: 1
            },
            {
                descricao: "Elevação Frontal",
                nome: "Elevação Frontal",
                series: 3,
                repeticoes: 10,
                descanso: 1
            },
            {
                descricao: "Encolhimento de Ombros (Trapézio)",
                nome: "Encolhimento de Ombros",
                series: 4,
                repeticoes: 8,
                descanso: 1
            }
        ],
        "Sexta": [
            {
                descricao: "Rosca Scott (Preacher Curl)",
                nome: "Rosca Scott",
                series: 4,
                repeticoes: 8,
                descanso: 1
            },
            {
                descricao: "Tríceps Testa (Skull Crushers)",
                nome: "Tríceps Testa",
                series: 3,
                repeticoes: 8,
                descanso: 1
            },
            {
                descricao: "Rosca Martelo (Hammer Curl)",
                nome: "Rosca Martelo",
                series: 3,
                repeticoes: 10,
                descanso: 1
            },
            {
                descricao: "Tríceps Corda (Cable Pushdown)",
                nome: "Tríceps Corda",
                series: 3,
                repeticoes: 10,
                descanso: 1
            },
            {
                descricao: "Prancha",
                nome: "Prancha",
                series: 3,
                repeticoes: 30,
                descanso: 60
            },
            {
                descricao: "Abdominais Crunch",
                nome: "Abdominais Crunch",
                series: 3,
                repeticoes: 15,
                descanso: 1
            }
        ]
    }
};
const treinoAvancado2 = {
    descricao: "Treino Avançado - Terceira Sequência",
    nome: "Treino Avançado",
    dias: {
        "Segunda": [
            {
                descricao: "Supino Reto",
                nome: "Supino Reto",
                series: 4,
                repeticoes: 8,
                descanso: 1
            },
            {
                descricao: "Supino Inclinado",
                nome: "Supino Inclinado",
                series: 3,
                repeticoes: 8,
                descanso: 1
            },
            {
                descricao: "Crucifixo (Flies)",
                nome: "Crucifixo",
                series: 3,
                repeticoes: 10,
                descanso: 1
            },
            {
                descricao: "Paralelas (Dips)",
                nome: "Paralelas",
                series: 3,
                repeticoes: 10,
                descanso: 1
            },
            {
                descricao: "Tríceps Testa (Skull Crushers)",
                nome: "Tríceps Testa",
                series: 4,
                repeticoes: 8,
                descanso: 1
            },
            {
                descricao: "Tríceps Corda (Cable Pushdown)",
                nome: "Tríceps Corda",
                series: 3,
                repeticoes: 10,
                descanso: 1
            }
        ],
        "Terca": [
            {
                descricao: "Puxada na Frente (Pull-Ups ou Lat Pulldown)",
                nome: "Puxada na Frente",
                series: 4,
                repeticoes: 8,
                descanso: 1
            },
            {
                descricao: "Barra Fixa (Chin-Ups)",
                nome: "Barra Fixa",
                series: 3,
                repeticoes: 8,
                descanso: 1
            },
            {
                descricao: "Pulldown com Pegada Aberta (Wide-Grip Pulldown)",
                nome: "Pulldown Pegada Aberta",
                series: 3,
                repeticoes: 10,
                descanso: 1
            },
            {
                descricao: "Remada Curvada",
                nome: "Remada Curvada",
                series: 4,
                repeticoes: 8,
                descanso: 1
            },
            {
                descricao: "Rosca Direta (Barbell Curl)",
                nome: "Rosca Direta",
                series: 3,
                repeticoes: 8,
                descanso: 1
            },
            {
                descricao: "Rosca Alternada (Alternating Dumbbell Curl)",
                nome: "Rosca Alternada",
                series: 3,
                repeticoes: 10,
                descanso: 1
            }
        ],
        "Quarta": [
            {
                descricao: "Agachamento Livre (Squat)",
                nome: "Agachamento Livre",
                series: 4,
                repeticoes: 8,
                descanso: 1
            },
            {
                descricao: "Leg Press",
                nome: "Leg Press",
                series: 3,
                repeticoes: 10,
                descanso: 1
            },
            {
                descricao: "Extensora de Pernas",
                nome: "Extensora de Pernas",
                series: 3,
                repeticoes: 10,
                descanso: 1
            },
            {
                descricao: "Flexora de Pernas",
                nome: "Flexora de Pernas",
                series: 3,
                repeticoes: 10,
                descanso: 1
            },
            {
                descricao: "Panturrilha em Pé (Calf Raises)",
                nome: "Panturrilha em Pé",
                series: 4,
                repeticoes: 12,
                descanso: 1
            }
        ],
        "Quinta": [
            {
                descricao: "Desenvolvimento Militar",
                nome: "Desenvolvimento Militar",
                series: 4,
                repeticoes: 8,
                descanso: 1
            },
            {
                descricao: "Elevação Lateral",
                nome: "Elevação Lateral",
                series: 3,
                repeticoes: 10,
                descanso: 1
            },
            {
                descricao: "Elevação Frontal",
                nome: "Elevação Frontal",
                series: 3,
                repeticoes: 10,
                descanso: 1
            },
            {
                descricao: "Encolhimento de Ombros (Trapézio)",
                nome: "Encolhimento de Ombros",
                series: 4,
                repeticoes: 8,
                descanso: 1
            }
        ],
        "Sexta": [
            {
                descricao: "Rosca Scott (Preacher Curl)",
                nome: "Rosca Scott",
                series: 4,
                repeticoes: 8,
                descanso: 1
            },
            {
                descricao: "Tríceps Testa (Skull Crushers)",
                nome: "Tríceps Testa",
                series: 3,
                repeticoes: 8,
                descanso: 1
            },
            {
                descricao: "Rosca Martelo (Hammer Curl)",
                nome: "Rosca Martelo",
                series: 3,
                repeticoes: 10,
                descanso: 1
            },
            {
                descricao: "Tríceps Corda (Cable Pushdown)",
                nome: "Tríceps Corda",
                series: 3,
                repeticoes: 10,
                descanso: 1
            },
            {
                descricao: "Prancha",
                nome: "Prancha",
                series: 3,
                repeticoes: 30,
                descanso: 1
            },
            {
                descricao: "Abdominais Crunch",
                nome: "Abdominais Crunch",
                series: 3,
                repeticoes: 15,
                descanso: 1
            }
        ]
    }
};
const treinoAvancado1 = {
    descricao: "Treino Intermediário - Segunda Sequência",
    nome: "Treino Intermediário",
    dias: {
        "Segunda": [
            {
                descricao: "Treino de Corpo Inteiro",
                nome: "Treino de Corpo Inteiro",
                exercicios: [
                    {
                        descricao: "Agachamento (Squat)",
                        nome: "Agachamento",
                        series: 4,
                        repeticoes: "8-10",
                        descanso: 1
                    },
                    {
                        descricao: "Supino Reto (Bench Press)",
                        nome: "Supino Reto",
                        series: 4,
                        repeticoes: "8-10",
                        descanso: 1
                    },
                    {
                        descricao: "Puxada na Frente (Pull-Ups ou Lat Pulldown)",
                        nome: "Puxada na Frente",
                        series: 4,
                        repeticoes: "8-10",
                        descanso: 1
                    },
                    {
                        descricao: "Rosca Direta (Barbell Curl)",
                        nome: "Rosca Direta",
                        series: 3,
                        repeticoes: "8-10",
                        descanso: 1
                    },
                    {
                        descricao: "Tríceps Testa (Skull Crushers)",
                        nome: "Tríceps Testa",
                        series: 3,
                        repeticoes: "8-10",
                        descanso: 1
                    }
                ]
            },
            // [ Continue ]
        ],
        "Terca": [
            {
                descricao: "Treino de Pernas e Core",
                nome: "Treino de Pernas e Core",
                exercicios: [
                    {
                        descricao: "Levantamento Terra (Deadlift)",
                        nome: "Levantamento Terra",
                        series: 4,
                        repeticoes: "6-8",
                        descanso: 2
                    },
                    {
                        descricao: "Leg Press",
                        nome: "Leg Press",
                        series: 3,
                        repeticoes: "8-10",
                        descanso: 1
                    },
                    {
                        descricao: "Extensora de Pernas",
                        nome: "Extensora de Pernas",
                        series: 3,
                        repeticoes: "10-12",
                        descanso: 1
                    },
                    {
                        descricao: "Prancha",
                        nome: "Prancha",
                        series: 3,
                        repeticoes: "30-60 segundos",
                        descanso: 1
                    },
                    {
                        descricao: "Abdominais Pendurados (Hanging Leg Raise)",
                        nome: "Abdominais Pendurados",
                        series: 3,
                        repeticoes: "12-15",
                        descanso: 1
                    }
                ]
            },
            // [ Continue ]
        ],
        "Quarta": [
            {
                descricao: "Treino de Ombros e Braços",
                nome: "Treino de Ombros e Braços",
                
                exercicios: [
                    {
                        descricao: "Desenvolvimento Militar",
                        nome: "Desenvolvimento Militar",
                        series: 4,
                        repeticoes: "8-10",
                        descanso: 1
                    },
                    {
                        descricao: "Elevação Lateral",
                        nome: "Elevação Lateral",
                        series: 3,
                        repeticoes: "10-12",
                        descanso: 1
                    },
                    {
                        descricao: "Encolhimento de Ombros (Trapézio)",
                        nome: "Encolhimento de Ombros",
                        series: 3,
                        repeticoes: "8-10",
                        descanso: 1
                    },
                    {
                        descricao: "Rosca Martelo (Hammer Curl)",
                        nome: "Rosca Martelo",
                        series: 3,
                        repeticoes: "8-10",
                        descanso: 1
                    },
                    {
                        descricao: "Tríceps Corda (Cable Pushdown)",
                        nome: "Tríceps Corda",
                        series: 3,
                        repeticoes: "8-10",
                        descanso: 1
                    }
                ]
            },
        ],
    }
};
treinoAvancado1.dias["Quinta"] = treinoAvancado1.dias["Segunda"];
treinoAvancado1.dias["Sexta"] = treinoAvancado1.dias["Terca"];
const treinoIntermediario = {
    descricao: "Treino Intermediário - Primeira Sequência",
    nome: "Treino Intermediário",
    dias: {
        "Segunda": [
            {
                descricao: "Treino de Corpo Inteiro",
                nome: "Treino de Corpo Inteiro",
                
                exercicios: [
                    {
                        descricao: "Agachamento (Squat)",
                        nome: "Agachamento",
                        series: 4,
                        repeticoes: "8-10",
                        descanso: 1
                    },
                    {
                        descricao: "Supino Reto (Bench Press)",
                        nome: "Supino Reto",
                        series: 4,
                        repeticoes: "8-10",
                        descanso: 1
                    },
                    {
                        descricao: "Puxada na Frente (Pull-Ups ou Lat Pulldown)",
                        nome: "Puxada na Frente",
                        series: 4,
                        repeticoes: "8-10",
                        descanso: 1
                    },
                    {
                        descricao: "Rosca Direta (Barbell Curl)",
                        nome: "Rosca Direta",
                        series: 3,
                        repeticoes: "8-10",
                        descanso: 1
                    },
                    {
                        descricao: "Tríceps Testa (Skull Crushers)",
                        nome: "Tríceps Testa",
                        series: 3,
                        repeticoes: "8-10",
                        descanso: 1
                    }
                ]
            },
            {
                // Continue with exercises for "Treino de Corpo Inteiro" on Segunda
            },
        ],
        "Terca": [
            {
                descricao: "Treino de Pernas e Core",
                nome: "Treino de Pernas e Core",
                
                exercicios: [
                    {
                        descricao: "Levantamento Terra (Deadlift)",
                        nome: "Levantamento Terra",
                        series: 4,
                        repeticoes: "6-8",
                        descanso: 2
                    },
                    {
                        descricao: "Leg Press",
                        nome: "Leg Press",
                        series: 3,
                        repeticoes: "8-10",
                        descanso: 1
                    },
                    {
                        descricao: "Extensora de Pernas",
                        nome: "Extensora de Pernas",
                        series: 3,
                        repeticoes: "10-12",
                        descanso: 1
                    },
                    {
                        descricao: "Prancha",
                        nome: "Prancha",
                        series: 3,
                        repeticoes: "30-60 segundos",
                        descanso: 1
                    },
                    {
                        descricao: "Abdominais Pendurados (Hanging Leg Raise)",
                        nome: "Abdominais Pendurados",
                        series: 3,
                        repeticoes: "12-15",
                        descanso: 1
                    }
                ]
            },
            // Continue with exercises for "Treino de Pernas e Core" on Terca
        ],
        "Quarta": [
            {
                descricao: "Treino de Ombros e Braços",
                nome: "Treino de Ombros e Braços",
                
                exercicios: [
                    {
                        descricao: "Desenvolvimento Militar",
                        nome: "Desenvolvimento Militar",
                        series: 4,
                        repeticoes: "8-10",
                        descanso: 1
                    },
                    {
                        descricao: "Elevação Lateral",
                        nome: "Elevação Lateral",
                        series: 3,
                        repeticoes: "10-12",
                        descanso: 1
                    },
                    {
                        descricao: "Encolhimento de Ombros (Trapézio)",
                        nome: "Encolhimento de Ombros",
                        series: 3,
                        repeticoes: "8-10",
                        descanso: 1
                    },
                    {
                        descricao: "Rosca Martelo (Hammer Curl)",
                        nome: "Rosca Martelo",
                        series: 3,
                        repeticoes: "8-10",
                        descanso: 1
                    },
                    {
                        descricao: "Tríceps Corda (Cable Pushdown)",
                        nome: "Tríceps Corda",
                        series: 3,
                        repeticoes: "8-10",
                        descanso: 1
                    }
                ]
            },
        ],
    }
};
treinoIntermediario.dias["Quinta"] = treinoIntermediario.dias["Segunda"];
treinoIntermediario.dias["Sexta"] = treinoIntermediario.dias["Terca"];


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
res.status(status).send(message);
};

const app = express();
const port = 3000;

app.use(express.json());

    app.get("/treinos-pre-preparados", (req, res) => {
    res.json([
        treinoIniciante,
        treinoIntermediario,
        treinoAvancado,
        treinoAvancado1,
        treinoAvancado2
    ]);
})

app.get('/users', async (req, res) => {
    const users = await getDocs(usersCol);
    console.log("Received get user list request")
    res.send(users.docs.map((doc) => doc.data()));
});

app.get('/user/:id', async (req, res) => {
    const id = req.params.id;
    const users = await getDocs(usersCol);
    const user = users.docs.find((doc) => doc.data().id === id);
    console.log("Received get user request with id: " + id)

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
    console.log("Received get user trainings request with id: " + id)
    if (user) {
        res.send(user.data().treinos);
    }
});

app.get("/user/:id/:trainingId", async (req, res) => {
    const id = req.params.id;
    const trainingId = req.params.trainingId;
    const users = await getDocs(usersCol);
    const user = users.docs.find((doc) => doc.data().id === id);
    console.log("Received get user training request with id: " + id + " and trainingId: " + trainingId)
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
    console.log("Received get user training days request with id: " + id + " and trainingId: " + trainingId)
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
    console.log("Received get user training day request with id: " + id + " and trainingId: " + trainingId + " and day: " + day)
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

app.get("/user/:id/:trainingId/:day/:exerciseId", async (req, res) => {
    const id = req.params.id;
    const trainingId = req.params.trainingId;
    const day = req.params.day;
    const exerciseId = req.params.exerciseId;
    const users = await getDocs(usersCol);
    const user = users.docs.find((doc) => doc.data().id === id);
    console.log("Received get user training exercise request with id: " + id + " and trainingId: " + trainingId + " and day: " + day + " and exerciseId: " + exerciseId)
    if (user) {
        const trainings = user.data().treinos;
        const training = trainings[trainingId];
        if (training) {
            const days = training.dias;
            const dayTrainings = days[day];
            if (dayTrainings) {
                const exercises = dayTrainings;
                const exercise = exercises[exerciseId];
                if (exercise) {
                    res.send(exercise);
                } else {
                    handleResponse(res, 404, 'Exercise does not exist');
                }
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
    console.log("Received post user request with data: " + req.body)
    if (!validateUser(newUser)) {
        handleResponse(res, 400, 'Invalid input data');
        console.log("Received data: " + newUser + " is invalid")
        debugger;
    } else {
        const exists = await existsUser(newUser.id);
        const existsEmail = await existsUserEmail(newUser.email);
        if (exists) {
            handleResponse(res, 409, 'User already exists');
        } else if (existsEmail) {
            handleResponse(res, 409, 'Email already exists');
        } else {
            newUser.treinos = [];
            newUser.treinoAtual = 0;
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
    console.log("Received post user training request with id: " + id + " and data: " + req.body)
    if (user) {
        newTraining.dias = {Segunda: [], Terca: [], Quarta: [], Quinta: [], Sexta: [], Sabado: [], Domingo: []};
        let data = user.data();
        data.treinos.push(newTraining);
        await setDoc(doc(db, 'user', id), data);
        res.status(201).send(data.treinos.length.toString());
    }
});

app.post('/user/:id/:trainingId/:day', async (req, res) => {
    const id = req.params.id;
    const trainingId = req.params.trainingId;
    const newDay = req.body;
    const users = await getDocs(usersCol);
    const user = users.docs.find((doc) => doc.data().id === id);
    console.log("Received post user training day request with id: " + id + " and trainingId: " + trainingId + " and data: " + req.body)
    if (user) {
        const data = user.data();
        const trainings = data.treinos[trainingId].dias[req.params.day];
        trainings.push(newDay);
        await setDoc(doc(db, 'user', id), data);
        res.status(201).send(trainings.length.toString());
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
            await updateDoc(doc(db, 'user', id), user.data());
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
            await updateDoc(doc(db, 'user', id), updatedUser);
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

app.delete('/user/:id/:trainingId', async (req, res) => {
    const id = req.params.id;
    const trainingId = req.params.trainingId;
    const users = await getDocs(usersCol);
    const user = users.docs.find((doc) => doc.data().id === id);
    console.log("Received delete user training request with id: " + id + " and trainingId: " + trainingId)
    if (user) {
        let data = user.data();
        const training = data.treinos[trainingId];
        if (training) {
            data.treinos.splice(trainingId, 1);
            await updateDoc(doc(db, 'user', id), data);
            handleResponse(res, 200, 'Training deleted');
        } else {
            handleResponse(res, 404, 'Training does not exist');
        }
    }
});

app.delete('/user/:id/:trainingId/:day', async (req, res) => {
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
            days.splice(day, 1);
            await updateDoc(doc(db, 'user', id), user.data());
            handleResponse(res, 200, 'Day deleted');
        } else {
            handleResponse(res, 404, 'Training does not exist');
        }
    }
});

app.delete('/user/:id/:trainingId/:day/:exerciseId', async (req, res) => {
    const id = req.params.id;
    const trainingId = req.params.trainingId;
    const day = req.params.day;
    const exerciseId = req.params.exerciseId;
    const users = await getDocs(usersCol);
    const user = users.docs.find((doc) => doc.data().id === id);
    if (user) {
        let data = user.data();
        const trainings = data.treinos;
        const training = trainings[trainingId];
        if (training) {
            const days = training.dias;
            const dayTrainings = days[day];
            if (dayTrainings) {
                const exercises = dayTrainings;
                exercises.splice(exerciseId, 1);
                await updateDoc(doc(db, 'user', id), data);
                handleResponse(res, 200, 'Exercise deleted');
            } else {
                handleResponse(res, 404, 'Day does not exist');
            }
        } else {
            handleResponse(res, 404, 'Training does not exist');
        }
    }
});

app.use((req, res) => {
    console.log("Received invalid request at path: \"" + req.path + "\"")
    handleResponse(res, 404, '404 Not Found');
});

app.listen(port, () => {
    console.log(`Example app listening at http://localhost:${port}`);
});
