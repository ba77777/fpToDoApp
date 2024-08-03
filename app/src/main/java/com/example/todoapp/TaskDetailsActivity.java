package com.example.todoapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.todoapp.data.Task;
import com.example.todoapp.ui.main.viewmodel.TaskViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TaskDetailsActivity extends AppCompatActivity {

    private TaskViewModel taskViewModel;
    private EditText editTextTitle, editTextDescription;
    private Button buttonSave, buttonDelete;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            finish();
            return;
        }

        taskViewModel = new ViewModelProvider(this, new TaskViewModel.Factory(getApplication(), currentUser)).get(TaskViewModel.class);

        Task task = (Task) getIntent().getSerializableExtra("task");

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonSave = findViewById(R.id.buttonSave);
        buttonDelete = findViewById(R.id.buttonDelete);

        if (task != null) {
            editTextTitle.setText(task.getTitle());
            editTextDescription.setText(task.getDescription());
        }

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (task != null) {
                    task.setTitle(editTextTitle.getText().toString());
                    task.setDescription(editTextDescription.getText().toString());
                    taskViewModel.update(task);
                }
                finish();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (task != null) {
                    taskViewModel.delete(task);
                }
                finish();
            }
        });
    }
}
