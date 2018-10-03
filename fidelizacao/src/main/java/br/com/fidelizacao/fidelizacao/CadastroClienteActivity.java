package br.com.fidelizacao.fidelizacao;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


import Model.Cliente;
import RestAdress.RestAddress;
import Task.HandlerTask;
import Task.HandlerTaskAdapter;
import Task.TaskRest;
import Util.JsonParser;
import Util.MaskFormattUtil;

public class CadastroClienteActivity extends AppCompatActivity {
    private TextInputEditText etNome, etCpf, etEmail, etCelular, etDataNascimento;
    private ProgressDialog progressDialog;
    private Context context;
    private ImageView ivCadastrar;
    private Toolbar toolbar;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private TextInputLayout textInputLayoutDataNascimento;
    private int ano, mes, dia;


    private static final int DATE_DIALOG_ID = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_cliente);

        context = this;

        etNome = findViewById(R.id.etNome);
        etCpf = findViewById(R.id.etCpf);
        etEmail = findViewById(R.id.etEmail);
        etCelular = findViewById(R.id.etCelular);
        etDataNascimento = findViewById(R.id.etDataNascimento);
        textInputLayoutDataNascimento = findViewById(R.id.textInputLayoutDataNascimento);

        textInputLayoutDataNascimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
            }
        });

        etDataNascimento.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                calendar = Calendar.getInstance();
                ano = calendar.get(Calendar.YEAR);
                mes = calendar.get(Calendar.MONTH);
                dia = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        etDataNascimento.setText(dia + "/" + mes + "/" + ano);
                        Toast.makeText(context, "Dia: " + dayOfMonth + " Mês: " + month + " ano: " + year, Toast.LENGTH_SHORT).show();
                    }
                }, ano, mes, dia);


                datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                etDataNascimento.clearFocus(); //Focus must be cleared so the value change listener is called
                                etDataNascimento.setText("");
                                datePickerDialog.dismiss();
                            }
                        });

                datePickerDialog.show();
            }
        });

        etDataNascimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "2", Toast.LENGTH_SHORT).show();

            }
        });



        ivCadastrar = findViewById(R.id.ivCadastrar);


        progressDialog = new ProgressDialog(this);

        toolbar = findViewById(R.id.toolbar);
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
        etCelular.addTextChangedListener(MaskFormattUtil.insert(etCelular, context));
        configurarOnKeyFocus();
    }


    private void configurarOnKeyFocus() {
        etNome.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {

                    if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
                        etNome.setFocusable(false);
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
                        etEmail.setFocusable(false);
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
                        etCpf.setFocusable(false);
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
                        etDataNascimento.setFocusable(false);
                        etEmail.setFocusable(true);
                        return true;
                    }

                }
                return false;
            }
        });

        ivCadastrar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {

                    if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
                        etDataNascimento.setFocusable(false);
                        ivCadastrar.setFocusable(true);
                        return true;
                    }

                }
                return false;
            }
        });

    }

    public void cadastrarClienteOnClick(View view) {
        String txtNome = etNome.getText().toString().trim();
        String txtCpf = etCpf.getText().toString().trim();
        String txtEmail = etEmail.getText().toString().trim();
        String txtCelular = etCelular.getText().toString().trim();
        String txtDataNascimento = etDataNascimento.getText().toString().trim();

        if (txtNome.isEmpty() || txtCpf.isEmpty() || txtEmail.isEmpty() || txtCelular.isEmpty() || txtDataNascimento.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
        } else {
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
            cliente.setStatus(true);


            new TaskRest(TaskRest.RequestMethod.POST, handlerCadastroCliente).execute(RestAddress.CADASTRAR_CLIENTE, new JsonParser<>(Cliente.class).fromObject(cliente));
        }
    }


    /* ======================== TASK =====================================*/
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
