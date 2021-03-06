package cgeo.geocaching.ui;

import cgeo.geocaching.R;
import cgeo.geocaching.activity.AbstractActivity;
import cgeo.geocaching.connector.UserAction;
import cgeo.geocaching.connector.UserAction.Context;

import android.support.annotation.NonNull;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

abstract class AbstractUserClickListener implements View.OnClickListener {

    private final List<UserAction> userActions;

    AbstractUserClickListener(@NonNull final List<UserAction> userActions) {
        this.userActions = userActions;
    }

    @Override
    public void onClick(final View view) {
        if (view == null) {
            return;
        }

        showUserActionsDialog(getUserName(view), view);
    }

    protected abstract String getUserName(View view);

    /**
     * Opens a dialog to do actions on an user name
     */
    private void showUserActionsDialog(final String userName, final View view) {
        if (userActions.isEmpty()) {
            return;
        }

        final AbstractActivity activity = (AbstractActivity) view.getContext();
        final Resources res = activity.getResources();

        final ArrayList<String> labels = new ArrayList<>(userActions.size());
        for (final UserAction action : userActions) {
            labels.add(res.getString(action.displayResourceId));
        }
        final CharSequence[] items = labels.toArray(new String[labels.size()]);

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(res.getString(R.string.user_menu_title) + " " + userName);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int item) {
                userActions.get(item).run(new Context(userName, activity));
            }
        });
        final AlertDialog alert = builder.create();
        alert.show();
    }

}
