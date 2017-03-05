package com.ninja.dexuscrisis.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ninja.dexuscrisis.R;


/**
 * The Adapter class for the ListView displayed in the left navigation drawer.
 */
public class LeftNavAdapter extends BaseAdapter
{

	/** The items. */
	private String titles[] = {"Phase 1 (Identify)", "Phase 2 (Respond)", "Phase 3 (Develop)", "When", "Who", "How"};

	/** The context. */
	private Context context;

	/** The selected. */
	private int selected;

	/**
	 * Setup the current selected position of adapter.
	 * 
	 * @param position
	 *            the new selection
	 */
	public void setSelection(int position)
	{
		selected = position;
		notifyDataSetChanged();
	}

	/**
	 * Instantiates a new left navigation adapter.
	 * 
	 * @param context
	 */
	public LeftNavAdapter(Context context)
	{
		this.context = context;

	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount()
	{
		return titles.length;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public String getItem(int arg0)
	{
		return titles[arg0];
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position)
	{
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
			convertView = LayoutInflater.from(context).inflate(
					R.layout.cell_left_nav_item, null);

		TextView tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);
		tvTitle.setText(titles[position]);

		return convertView;
	}

}
