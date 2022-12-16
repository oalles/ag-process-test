package com.example.agprocesstest.ui;


import com.example.agprocesstest.sources.Source;
import com.example.agprocesstest.sources.SourceService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.util.Optional;

@SpringComponent
@UIScope
public class SourceEditor extends VerticalLayout implements KeyNotifier {

    private final SourceService sourceService;
    private Source source;

    // Editable Source properties
    TextField name = new TextField("Name");

    Button save = new Button("Save", VaadinIcon.CHECK.create());
    Button cancel = new Button("Cancel", VaadinIcon.CLOSE.create());
    Button delete = new Button("Delete", VaadinIcon.TRASH.create());

    HorizontalLayout actions = new HorizontalLayout(save, delete, cancel);

    Binder<Source> binder = new Binder<>(Source.class);
    ChangeHandler changeHandler;

    public SourceEditor(SourceService sourceService) {
        this.sourceService = sourceService;

        name.setSizeFull();

        add(name, actions);
        setHorizontalComponentAlignment(Alignment.END, actions);

        binder.bindInstanceFields(this);
        setSpacing(true);
//        getStyle().set("background", "var(--app-layout-badge-background)")
//                .set("color", "var(--app-layout-badge-font-color)")
//                .set("border-radius", "25px");

        save.getElement().getThemeList().add("primary");

        addKeyPressListener(Key.ENTER, e -> save());

        save.addClickListener(e -> save());
        save.getElement().getThemeList().add("primary");
        delete.addClickListener(e -> delete());
        delete.getElement().getThemeList().add("error");
        cancel.addClickListener(e -> cancel());
        setVisible(false);
    }

    void cancel() {
        editSource(null);
        changeHandler.onChange();
    }

    void delete() {
        try {
            sourceService.delete(source);
            showSuccessNotification("Source succesfully deleted");
            editSource(null);
            changeHandler.onChange();
        } catch (Exception e) {
            // TODO: Full Error handler based on exception type
            showErrorNotification("Source cannot be deleted");
        }
    }

    void save() {
        try {
            source = sourceService.save(source);
            showSuccessNotification("Source succesfully updated");
            changeHandler.onChange();
        } catch (Exception e) {
            // TODO: Full Error handler based on exception type
            showErrorNotification("Source cannot be saved");
        }
    }

    void showErrorNotification(String message) {
        if (UI.getCurrent() != null) {
            UI.getCurrent().access(() -> {
                Notification notif = Notification.show(message, 3000, Notification.Position.MIDDLE);
                notif.addThemeVariants(NotificationVariant.LUMO_ERROR);
            });
        }
    }

    void showSuccessNotification(String message) {
        if (UI.getCurrent() != null) {
            UI.getCurrent().access(() -> {
                Notification notif = Notification.show(message, 3000, Notification.Position.MIDDLE);
                notif.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            });
        }
    }

    public final void editSource(Source src) {
        if (src == null) {
            setVisible(false);
            return;
        }
        final var persisted = src.getId() != null;
        if (persisted) {
            // Find fresh entity for editing
            Optional<Source> sourceOptional = sourceService.findById(src.getId());
            if (!sourceOptional.isPresent()) {
                // TODO: Comminicate Inconsistency
            } else {
                source = sourceOptional.get();
            }
        } else {
            source = src;
        }

        // Bind customer properties to similarly named fields
        binder.setBean(source);

        setVisible(true);

        // Focus first name initially
        name.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        // ChangeHandler is notified when either save or delete
        // is clicked
        changeHandler = h;
    }

    public interface ChangeHandler {
        void onChange();
    }
}
