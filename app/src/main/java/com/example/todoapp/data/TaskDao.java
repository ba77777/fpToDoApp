package com.example.todoapp.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class TaskDao {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference tasksCollection;

    public TaskDao(FirebaseUser currentUser) {
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        String userId = currentUser.getUid();
        tasksCollection = db.collection("users").document(userId).collection("tasks");
    }

    public LiveData<List<Task>> getAllTasks() {
        MutableLiveData<List<Task>> tasksLiveData = new MutableLiveData<>();
        tasksCollection.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                return;
            }
            List<Task> tasks = new ArrayList<>();
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                tasks.add(doc.toObject(Task.class));
            }
            tasksLiveData.setValue(tasks);
        });
        return tasksLiveData;
    }

    public void insertTask(Task task) {
        tasksCollection.add(task);
    }

    public void deleteTask(Task task) {
        tasksCollection.document(task.getId()).delete();
    }

    public void updateTask(Task task) {
        tasksCollection.document(task.getId()).set(task);
    }
}
