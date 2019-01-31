package reminderoli.reminder_oli;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MobilAdapter extends RecyclerView.Adapter<MobilAdapter.ViewHolder> {
    ArrayList<Mobil> listUser;
    Context context;

    public ArrayList<Mobil> getListUser() {
        return listUser;
    }

    public void setList(ArrayList<Mobil> listUser) {
        this.listUser = listUser;
    }

    public MobilAdapter(Context context) {

        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {


        viewHolder.kmAkhir.setText(getListUser().get(i).getKmService());
        viewHolder.kmAwal.setText(getListUser().get(i).getKmSekarang());
        viewHolder.mIdUser.setText(getListUser().get(i).getId_user() + " - " + getListUser().get(i).getNamaUser());
        viewHolder.mIdOli.setText(getListUser().get(i).getId_oli());
        viewHolder.mNopol.setText(getListUser().get(i).getNoPol());
        viewHolder.namaMobil.setText(getListUser().get(i).getNamaMobil());
        viewHolder.jenisMobil.setText(getListUser().get(i).getJenisMobil());

    }

    @Override
    public int getItemCount() {
        return getListUser().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.idUseritem)
        TextView mIdUser;
        @BindView(R.id.noPolitem)
        TextView mNopol;
        @BindView(R.id.jenisOliItem)
        TextView mIdOli;
        @BindView(R.id.kilometer_sekarang)
        TextView kmAwal;
        @BindView(R.id.kilometer_service)
        TextView kmAkhir;
        @BindView(R.id.jenisMobilItem)
        TextView jenisMobil;
        @BindView(R.id.namaMobilItem)
        TextView namaMobil;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
