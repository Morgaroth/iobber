package pl.edu.agh.iobber.android.utils;

//public class ContactListCursorAdapter extends SimpleCursorAdapter implements Filterable {
//
//    private Context context;
//
//    private int layout;
//
//    public ContactListCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
//        super(context, layout, c, from, to);
//        this.context = context;
//        this.layout = layout;
//    }
//
//    @Override
//    public View newView(Context context, Cursor cursor, ViewGroup parent) {
//
//        Cursor c = getCursor();
//
//        final LayoutInflater inflater = LayoutInflater.from(context);
//        View v = inflater.inflate(layout, parent, false);
//
//        int nameCol = c.getColumnIndex(Contacts.People.NAME);
//
//        String name = c.getString(nameCol);
//
//        /**
//         * Next set the name of the entry.
//         */
//        TextView name_text = (TextView) v.findViewById(R.id.name_entry);
//        if (name_text != null) {
//            name_text.setText(name);
//        }
//
//        return v;
//    }
//
//    @Override
//    public void bindView(View v, Context context, Cursor c) {
//
//        int nameCol = c.getColumnIndex(Contacts.People.NAME);
//
//        String name = c.getString(nameCol);
//
//        /**
//         * Next set the name of the entry.
//         */
//        TextView name_text = (TextView) v.findViewById(R.id.name_entry);
//        if (name_text != null) {
//            name_text.setText(name);
//        }
//    }
//
//    @Override
//    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
//        if (getFilterQueryProvider() != null) {
//            return getFilterQueryProvider().runQuery(constraint);
//        }
//
//        StringBuilder buffer = null;
//        String[] args = null;
//        if (constraint != null) {
//            buffer = new StringBuilder();
//            buffer.append("UPPER(");
//            buffer.append(Contacts.People.NAME);
//            buffer.append(") GLOB ?");
//            args = new String[]{constraint.toString().toUpperCase() + "*"};
//        }
//
//        return context.getContentResolver().query(Contacts.People.CONTENT_URI, null,
//                buffer == null ? null : buffer.toString(), args, Contacts.People.NAME + " ASC");
//    }
//}