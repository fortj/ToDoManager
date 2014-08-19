package course.labs.todomanager;

import java.util.ArrayList;
import java.util.List;
import proj.todomanager.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import course.labs.todomanager.ToDoItem.Status;

public class ToDoListAdapter extends BaseAdapter {

	// List of ToDoItems
	private final List<ToDoItem> mItems = new ArrayList<ToDoItem>();
	
	private final Context mContext;
	private final Activity mActivity;

	private static final String TAG = "Lab-UserInterface";

	public ToDoListAdapter(Context context, Activity activity) {

		mContext = context;
		mActivity = activity;

	}

	// Add a ToDoItem to the adapter
	// Notify observers that the data set has changed

	public void add(ToDoItem item) {

		mItems.add(item);
		notifyDataSetChanged();

	}
	
	// Clears the list adapter of all items.
	
	public void clear(){

		mItems.clear();
		notifyDataSetChanged();
	
	}

	// Returns the number of ToDoItems

	@Override
	public int getCount() {

		return mItems.size();

	}

	// Retrieve the number of ToDoItems

	@Override
	public Object getItem(int pos) {

		return mItems.get(pos);

	}

	// Get the ID for the ToDoItem
	// In this case it's just the position

	@Override
	public long getItemId(int pos) {

		return pos;

	}
	
		
	//Create a View to display the ToDoItem 
	// at specified position in mItems
	private void deleteToDoItem(ToDoItem toDoItem) {
		final ToDoItem item = toDoItem;
		
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
		builder.setTitle("Confirm Delete");
		builder.setMessage("Delete?");
		builder.setCancelable(true);
		//alertDialog.setIcon(R.drawable.delete);

		builder.setPositiveButton("Yes", 
				new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					Log.i("DEBUG", "Yes clicked");
					dialog.cancel();
					mItems.remove(item);
					notifyDataSetChanged();
					//Toast.makeText(mContext,  "Yes clicked", Toast.LENGTH_SHORT).show();
				}
		});				

		builder.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					Log.i("DEBUG", "No clicked");
					dialog.cancel();
				}						
		});
		
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {


		//TODO - Get the current ToDoItem
		final ToDoItem toDoItem = (ToDoItem) getItem(position);

		//TODO - Inflate the View for this ToDoItem
		// from todo_item.xml.
		RelativeLayout itemLayout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.todo_item, parent, false);
		
		
		//TODO - Fill in specific ToDoItem data
		// Remember that the data that goes in this View
		// corresponds to the user interface elements defined 
		// in the layout file 

		//TODO - Display Title in TextView

		final TextView titleView = (TextView) itemLayout.findViewById(R.id.titleView);
		titleView.setText(toDoItem.getTitle());
		
		titleView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("DEBUG:", "titleView received Click");
			}
		});
		
		
		// TODO - Set up Status CheckBox
	
		final CheckBox statusView = (CheckBox) itemLayout.findViewById(R.id.statusCheckBox);
		if (toDoItem.getStatus() == Status.DONE) {
			statusView.setChecked(true);
			statusView.getRootView().setBackgroundColor(Color.GRAY);
		}
		

		
		final Spinner spinner = (Spinner) itemLayout.findViewById(R.id.prioritySpinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext, R.array.priority_array, android.R.layout.simple_spinner_item);
		spinner.setAdapter(adapter);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		final int i = toDoItem.getPriorityIndex(toDoItem.getPriority());
		spinner.setSelection(i); //Show selected priority 	
		
		statusView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				View pv = (View) statusView.getParent();

				//log("Entered onCheckedChanged()");
				// TODO - Set up and implement an OnCheckedChangeListener, which 
				// is called when the user toggles the status checkbox
				if (statusView.isChecked()) {
//					Log.i(TAG, "was not Checked!");
					toDoItem.setStatus(Status.DONE);
					statusView.setChecked(true);
					//change color to de-emphsize todo item when done
					pv.setBackgroundColor(Color.GRAY);		
					spinner.setVisibility(View.GONE);
				}
				else {
					toDoItem.setStatus(Status.NOTDONE);
					statusView.setChecked(false);
					//change color back to normal strength to show active todo item
					pv.setBackgroundColor(Color.BLACK);
					spinner.setVisibility(View.VISIBLE);
				}
			}

			
		});
		

				

		//TODO - Display Priority in a TextView
//		Spinner spinner = (Spinner) itemLayout.findViewById(R.id.prioritySpinner);
						
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				TextView v = (TextView) selectedItemView;
				int c; //color

				if (position == 0) {
					c = Color.RED;
				}
				else if (position == 1) {
					c = Color.YELLOW;
				}
				else { 
					c = Color.WHITE;
				}
				
				//Log.i("DEBUG", "position="+position);
				spinner.setSelection(position); //Show selected priority 	
				spinner.performItemClick(selectedItemView, position, id);
				v.setTextColor(c);
			}				
			
			public void onNothingSelected(AdapterView<?> parentView) {
				Log.i("DEBUG", "onNothingSelected");
				
			};
		});
		
		
		
		// TODO - Display Time and Date. 
		// Hint - use ToDoItem.FORMAT.format(toDoItem.getDate()) to get date and time String

		final TextView dateView = (TextView) itemLayout.findViewById(R.id.dateView);	
		dateView.setText(ToDoItem.FORMAT.format(toDoItem.getDate()));
		

		//Remove ToDoItems on long click
		itemLayout.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				deleteToDoItem(toDoItem);
				Log.i("DEBUG", "long click detected in itemLayout");						
				return true;
			}
		});
	
		titleView.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				deleteToDoItem(toDoItem);
				Log.i("DEBUG", "long click detected in titleView");
				return true;
			}
		}
		);
		
		
		
		// Return the View you just created
		return itemLayout;

	}
	
	private void log(String msg) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Log.i(TAG, msg);
	}

}
