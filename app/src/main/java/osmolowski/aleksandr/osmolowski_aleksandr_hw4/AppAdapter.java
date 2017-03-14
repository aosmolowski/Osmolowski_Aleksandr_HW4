package osmolowski.aleksandr.osmolowski_aleksandr_hw4;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 13.03.2017.
 */

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder> implements Filterable
{

    private Context context;
    private List<ApplicationInfo> data;
    private List<ApplicationInfo> dataFiltered;
    private PackageManager packageManager;
    private AppFilter appFilter = new AppFilter();

    public AppAdapter(Context context, List<ApplicationInfo> data) {
        this.context = context;
        this.data = data;
        this.dataFiltered = data;
        this.packageManager = context.getPackageManager();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.recycler_item, null);
        view.setOnClickListener(clkLstn);
        return new ViewHolder(view);
    }

    View.OnClickListener clkLstn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView txtPkgView = (TextView) v.findViewById(R.id.app_package);
            try {
                Intent intent = packageManager.getLaunchIntentForPackage(
                        txtPkgView.getText().toString());
                if(intent != null) {
                    context.startActivity(intent);
                }
            } catch(ActivityNotFoundException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            } catch(Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }
    };

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ApplicationInfo appInf = dataFiltered.get(position);

        holder.appName.setText(appInf.loadLabel(packageManager));
        holder.appPkg.setText(appInf.packageName);
        holder.appIcon.setImageDrawable(appInf.loadIcon(packageManager));
    }

    @Override
    public int getItemCount() {
        return dataFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return appFilter;
    }

    class AppFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint.toString().trim().isEmpty()) {
                dataFiltered = new ArrayList<>(data);
                return null;
            }

            dataFiltered = new ArrayList<>();
            for (ApplicationInfo item: data) {
                if (item.loadLabel(packageManager).toString().toLowerCase().contains(
                        constraint.toString().toLowerCase())) dataFiltered.add(item);
            }

            return null;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notifyDataSetChanged();
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView appIcon;
        TextView appName;
        TextView appPkg;

        public ViewHolder(View itemView) {
            super(itemView);

            this.appIcon = (ImageView) itemView.findViewById(R.id.app_icon);
            this.appName = (TextView) itemView.findViewById(R.id.app_name);
            this.appPkg = (TextView) itemView.findViewById(R.id.app_package);
        }
    }
}
