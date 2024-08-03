package com.example.todoapp.ui.main.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.todoapp.data.Task;
import com.example.todoapp.data.TaskDao;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;

    public TaskViewModel(@NonNull Application application, FirebaseUser currentUser) {
        super(application);
        taskDao = new TaskDao(currentUser);
        allTasks = taskDao.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public void insert(Task task) {
        taskDao.insertTask(task);
    }

    public void delete(Task task) {
        taskDao.deleteTask(task);
    }

    public void update(Task task) {
        taskDao.updateTask(task);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;
        private final FirebaseUser currentUser;

        public Factory(@NonNull Application application, FirebaseUser currentUser) {
            this.application = application;
            this.currentUser = currentUser;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(TaskViewModel.class)) {
                return (T) new TaskViewModel(application, currentUser);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

}
