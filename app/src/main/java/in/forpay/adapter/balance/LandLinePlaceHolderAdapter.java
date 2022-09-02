package in.forpay.adapter.balance;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import in.forpay.R;
import in.forpay.databinding.AdapterLandlinePlaceholderBinding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.response.GetServiceList;
import in.forpay.util.ProgressHelper;

public class LandLinePlaceHolderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ItemClickListener listener;
    private ArrayList<GetServiceList.ServiceBean.ParamsBean> arrayList;
    private String selectedId="";
    private ProgressHelper progressHelper;
    private String billamt;
    private String billname;
    private String mobile,inputValue1,inputValue2;
    private int selectedItem;

    public LandLinePlaceHolderAdapter(Context context, ArrayList<GetServiceList.ServiceBean.ParamsBean> arrayList, ItemClickListener listener,String value,String inputValue1,String inputValue2) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
        this.mobile=value;
        this.inputValue1=inputValue1;
        this.inputValue2=inputValue2;
        selectedItem = 0;

        progressHelper = new ProgressHelper((Activity) context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adView = LayoutInflater.from(context).inflate(R.layout.adapter_landline_placeholder, parent, false);
        return new MyViewHolder(adView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            try {
                MyViewHolder viewHolder = (MyViewHolder) holder;
                GetServiceList.ServiceBean.ParamsBean model = (GetServiceList.ServiceBean.ParamsBean) arrayList.get(position);
                viewHolder.binding.editText.setHint(model.getPlaceHolder());
                viewHolder.binding.label.setText(model.getLabel());
                if (position == 0) {
                    viewHolder.binding.editText.setText(mobile);
                }
                else if(position==1){
                    viewHolder.binding.editText.setText(inputValue1);
                }
                else if(position==2){
                    viewHolder.binding.editText.setText(inputValue2);
                }

                switch (model.getInputType()) {
                    case "text":
                    case "ALPHANUMERIC":
                        viewHolder.binding.editText.setInputType(InputType.TYPE_CLASS_TEXT);
                        viewHolder.binding.editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(model.getMaxLength()))});

                        break;
                    case "number":
                    case "NUMERIC":
                        viewHolder.binding.editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                        viewHolder.binding.editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(model.getMaxLength()))});

                        break;
                    case "date":
                        dateOfBirthType(model,viewHolder);
                        break;

                }

                dateOfBirthType(model,viewHolder);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void dateOfBirthType(GetServiceList.ServiceBean.ParamsBean model, MyViewHolder viewHolder){
        if (model.getPlaceHolder().equalsIgnoreCase("Date Of Birth")){
            viewHolder.binding.editText.setInputType(InputType.TYPE_CLASS_DATETIME);
            viewHolder.binding.editText.setCursorVisible(false);
            viewHolder.binding.editText.setLongClickable(false);
            viewHolder.binding.editText.setClickable(false);
            viewHolder.binding.editText.setFocusable(false);
            viewHolder.binding.editText.setSelected(false);
            viewHolder.binding.editText.setKeyListener(null);
        }
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void setItems(String selectedId){
        this.selectedId=selectedId;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterLandlinePlaceholderBinding binding;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = AdapterLandlinePlaceholderBinding.bind(itemView);

            binding.editText.setOnClickListener(view -> {
                if (arrayList.get(getAdapterPosition()).getPlaceHolder().equalsIgnoreCase("Date Of Birth")) {
                    selectDate(binding.editText);
                }
            });

            binding.editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    listener.onItemClick(getAdapterPosition(), charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }

        /**
         * Click on select time
         */
        private void selectDate(final EditText editText) {

            final Calendar calendar = Calendar.getInstance();
            final int hour = calendar.get(Calendar.HOUR_OF_DAY);
            final int minute = calendar.get(Calendar.MINUTE);

            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    String myFormat = "yyyy-MM-dd"; // your format
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                    String date = sdf.format(calendar.getTime());
                    editText.setText(date);
                }
            };
            DatePickerDialog dialog = new DatePickerDialog(context, R.style.DatePickerDialogThemeBalance, date,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
            // Set min date limit
            dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        }
    }
}