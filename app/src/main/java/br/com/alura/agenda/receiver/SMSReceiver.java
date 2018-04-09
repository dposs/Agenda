package br.com.alura.agenda.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.Toast;

import br.com.alura.agenda.R;
import br.com.alura.agenda.dao.AlunoDAO;
import br.com.alura.agenda.model.Aluno;

public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage[] smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        String telefone = smsMessages[0].getDisplayOriginatingAddress();

        telefone = telefone.replaceAll("^\\+\\d\\d", "");

        AlunoDAO dao = new AlunoDAO(context);
        Aluno aluno = dao.getByTelefone(telefone);

        if (aluno != null) {
            Toast.makeText(context, "SMS Recebido do aluno(a) " + aluno.getNome(), Toast.LENGTH_LONG).show();
            MediaPlayer mp = MediaPlayer.create(context, R.raw.msg);
            mp.start();
        }

        dao.close();
    }
}
