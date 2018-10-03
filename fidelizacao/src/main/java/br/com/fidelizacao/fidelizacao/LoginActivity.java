package br.com.fidelizacao.fidelizacao;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteBlobTooBigException;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import Model.Cliente;
import RestAdress.RestAddress;
import Task.HandlerTask;
import Task.HandlerTaskAdapter;
import Task.TaskRest;
import Util.JsonParser;
import Util.MaskFormattUtil;

public class LoginActivity extends AppCompatActivity {
    private Context context;
    private EditText etCpf;
    private ProgressDialog dialog;
    private AlertDialog.Builder builder;
    private ImageView ivLogin;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;

        etCpf = findViewById(R.id.etCpfLogin);
        dialog = new ProgressDialog(this);
        builder = new AlertDialog.Builder(this);
        ivLogin = findViewById(R.id.ivLogin);

        //Pegando a referencia do toolbar
        toolbar = findViewById(R.id.toolbar);

        //Se o toolbar nao for nullo infla ele no lugar do ActionBar
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }



    }

    @Override
    protected void onStart() {
        super.onStart();
        etCpf.addTextChangedListener(MaskFormattUtil.insert(etCpf, context));
        etCpf.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {

                    if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
                        ivLogin.setFocusable(true);
                        return true;
                    }

                }
                return false;
            }
        });
    }

    public void loginOnClick(View view) {
        String txtCpf = etCpf.getText().toString().trim();

        if (txtCpf.isEmpty()) {
            Toast.makeText(context, "Preencha o seu CPF", Toast.LENGTH_SHORT).show();
        } else {
            JsonParser<Cliente> parser = new JsonParser<>(Cliente.class);
            Cliente cliente = new Cliente();
            cliente.setCpf(txtCpf);

            Log.e("CPF", txtCpf);
            new TaskRest(TaskRest.RequestMethod.POST, handlerLogin).execute(RestAddress.LOGIN, parser.fromObject(cliente));
        }
    }

    private HandlerTask handlerLogin = new HandlerTaskAdapter() {
        @Override
        public void onPreHandle() {
            super.onPreHandle();
            dialog.setMessage("Validando informações...");
            dialog.show();
        }

        @Override
        public void onSuccess(String valueRead) {
            super.onSuccess(valueRead);
            Log.e("TAG", valueRead);
            Intent intent = new Intent(context, EfetuarFidelidadeActivity.class);
            intent.putExtra("clienteLogado", valueRead);
            startActivity(intent);
            dialog.dismiss();
            finish();
        }

        @Override
        public void onError(Exception erro) {
            super.onError(erro);
            dialog.dismiss();
            if (erro.getMessage().trim().equals("Erro: 401")) {
                configurarDialogCadastro();

            } else {
                Toast.makeText(context, erro.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void configurarDialogCadastro() {
        builder.setMessage("Deseja se cadastrar?")
                .setTitle("Verificamos que você não tem um cadastro!");
        builder.setCancelable(false);
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(context, CadastroClienteActivity.class));
                finish();
            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(context, MainActivity.class));
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
