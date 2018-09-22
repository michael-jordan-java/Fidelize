package br.com.fidelizacao.fidelizacao;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


import Model.Cliente;
import RestAdress.RestAddress;
import Task.HandlerTask;
import Task.HandlerTaskAdapter;
import Task.TaskRest;
import Util.JsonParser;
import Util.MaskFormattUtil;

public class CadastroClienteActivity extends AppCompatActivity {
    private EditText etNome, etCpf, etEmail, etCelular, etDataNascimento;
    private ProgressDialog progressDialog;
    private Context context;
    private ImageView ivCadastrar;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_cliente);

        etNome = findViewById(R.id.etNome);
        etCpf = findViewById(R.id.etCpf);
        etEmail = findViewById(R.id.etEmail);
        etCelular = findViewById(R.id.etCelular);
        etDataNascimento = findViewById(R.id.etDataNascimento);

        context = this;

        progressDialog = new ProgressDialog(this);

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        etCpf.addTextChangedListener(MaskFormattUtil.insert(etCpf, context));
        etCelular.addTextChangedListener(MaskFormattUtil.insert(etCelular, context));
        configurarOnKeyFocus();
    }

    private void configurarOnKeyFocus() {
        etNome.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {

                    if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
                        etEmail.setFocusable(true);
                        return true;
                    }

                }
                return false;
            }
        });

        etEmail.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {

                    if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
                        etCpf.setFocusable(true);
                        return true;
                    }

                }
                return false;
            }
        });

        etCpf.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {

                    if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
                        etCelular.setFocusable(true);
                        return true;
                    }

                }
                return false;
            }
        });

        etDataNascimento.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {

                    if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
                        etEmail.setFocusable(true);
                        return true;
                    }

                }
                return false;
            }
        });
    }

    public void cadastrarClienteOnClick(View view){
        String txtNome = etNome.getText().toString().trim();
        String txtCpf = etCpf.getText().toString().trim();
        String txtEmail = etEmail.getText().toString().trim();
        String txtCelular = etCelular.getText().toString().trim();
        String txtDataNascimento = etDataNascimento.getText().toString().trim();

        if(txtNome.isEmpty() || txtCpf.isEmpty() || txtEmail.isEmpty() || txtCelular.isEmpty() || txtDataNascimento.isEmpty()){
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
        }else{
            Cliente cliente = new Cliente();
            cliente.setNome(txtNome);
            cliente.setCpf(txtCpf);
            cliente.setEmail(txtEmail);
            cliente.setCelular(txtCelular);

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date dataFormatada = null;
            try {
                dataFormatada = format.parse(txtDataNascimento);
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            cliente.setDataNascimento(dataFormatada);
            cliente.setPrimeiraCompra(true);

            new TaskRest(TaskRest.RequestMethod.POST, handlerCadastroCliente).execute(RestAddress.CADASTRAR_CLIENTE, new JsonParser<>(Cliente.class).fromObject(cliente));
        }
    }

    private HandlerTask handlerCadastroCliente = new HandlerTaskAdapter() {
        @Override
        public void onPreHandle() {
            super.onPreHandle();
            progressDialog.setTitle("Cadastrando.....");
            progressDialog.setMessage("Irá levar alguns segundos");
            progressDialog.show();
        }

        @Override
        public void onSuccess(String valueRead) {
            super.onSuccess(valueRead);
            progressDialog.dismiss();
            Toast.makeText(CadastroClienteActivity.this, valueRead, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(Exception erro) {
            super.onError(erro);
            progressDialog.dismiss();
            Toast.makeText(CadastroClienteActivity.this, erro.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

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
