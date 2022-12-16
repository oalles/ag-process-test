package com.example.agprocesstest.ui;

import com.example.agprocesstest.events.Event;
import com.example.agprocesstest.events.EventService;
import com.example.agprocesstest.sources.Source;
import com.example.agprocesstest.sources.SourceService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.TimeZone;

@Route
public class MainView extends VerticalLayout {

    private SourceService sourceService;
    private EventService eventService;

    private SourceEditor sourceEditor;

    final Grid<Source> sourcesGrid;
    final Grid<Event> eventsGrid;

    final TextField filterBySourcesName;
    private final Button addSourceBtn;
    private final Button editSourceBtn;

    public MainView(SourceService sourceService, EventService eventService, SourceEditor sourceEditor) {

        this.sourceService = sourceService;
        this.eventService = eventService;
        this.sourceEditor = sourceEditor;

        setSizeFull();

        var container = new VerticalLayout();
        container.setMaxWidth("700px");
        add(container);
        setHorizontalComponentAlignment(Alignment.CENTER, container);

        sourcesGrid = new Grid<>(Source.class);
        sourcesGrid.setHeight("200px");
        sourcesGrid.setMultiSort(false);
        sourcesGrid.addSortListener((event) -> {
            Sort sort = null;
            if (!event.getSortOrder().isEmpty()) {
                sort = buildSortFromSortOrderSources(event.getSortOrder().get(0));
            }
            fetchSources(null, sort);
        });

        eventsGrid = new Grid<>(Event.class, false); // Prevents autogeneration of columns so we can add the timestamp renderer
        eventsGrid.setHeight("400px");
        eventsGrid.setMultiSort(false);
        eventsGrid.addColumn("id").setWidth("75px").setFlexGrow(0);
        eventsGrid.addColumn("source.name").setHeader("Source").setWidth("175px").setFlexGrow(0);

        var renderer = new LocalDateTimeRenderer<Event>(e -> LocalDateTime.ofInstant(e.getTs(), TimeZone.getDefault().toZoneId()),
                "yyyy-MM-dd HH:mm:ss");
        eventsGrid
                .addColumn(renderer)
                .setHeader("Timestamp");
        eventsGrid.addColumn("val").setHeader("Value").setWidth("100px").setFlexGrow(0);

        filterBySourcesName = new TextField();
        filterBySourcesName.setPlaceholder("Filter by source name");

        addSourceBtn = new Button("Add Source", VaadinIcon.PLUS.create());
        addSourceBtn.addClickListener(e -> openSourceEditor(new Source()));
        editSourceBtn = new Button("Edit Source", VaadinIcon.PENCIL.create());
        editSourceBtn.setVisible(false);
        editSourceBtn.getElement().getThemeList().add("primary");
        editSourceBtn.addClickListener(e -> openSourceEditor(sourcesGrid.asSingleSelect().getValue()));

        // Layouts
        var sourcesLayout = new VerticalLayout();
        var sourceHeaderTools = new HorizontalLayout(addSourceBtn, editSourceBtn);
        sourcesLayout.add(sourceHeaderTools, sourcesGrid);

        var eventsLayout = new VerticalLayout();
        var eventHeaderTools = new HorizontalLayout(filterBySourcesName);
        eventsLayout.add(eventHeaderTools, eventsGrid);

        container.add(sourcesLayout, sourceEditor, eventsLayout);

        filterBySourcesName.setValueChangeMode(ValueChangeMode.EAGER);
        filterBySourcesName.addFocusListener(e -> {
            if (!sourcesGrid.getSelectedItems().isEmpty()) {
                sourcesGrid.getSelectedItems().stream().forEach(i -> sourcesGrid.deselect(i));
            }
        });
        filterBySourcesName.addValueChangeListener(e -> {
            fetchEvents(e.getValue(), null);
        });

        sourcesGrid.asSingleSelect().addValueChangeListener(e -> {
            filterBySourcesName.clear();
            if (e.getValue() != null) {
                editSourceBtn.setVisible(true);
                fetchEvents(e.getValue().getName(), null);
            } else {
                editSourceBtn.setVisible(false);
                fetchSources(null, null);
                fetchEvents(null, null);
            }
        });

        eventsGrid.setSelectionMode(Grid.SelectionMode.NONE);
        eventsGrid.setSortableColumns("id");
        eventsGrid.addSortListener((event) -> {
            Sort sort = null;
            if (!event.getSortOrder().isEmpty()) {
                sort = buildSortFromSortOrderEvents(event.getSortOrder().get(0));
            }
            fetchEvents(null, sort);
        });

        sourceEditor.setChangeHandler(() -> {
            sourceEditor.setVisible(false);
            addSourceBtn.setEnabled(true);
            editSourceBtn.setEnabled(true);
            fetchSources(null, null);
            fetchEvents(null, null);
        });

        // fetch data
        fetchSources(null, null);
        fetchEvents(null, null);
    }

    private void fetchSources(String sourceName, final Sort sort) {
        if (!StringUtils.hasText(sourceName)) {
            sourceName = "";
        }
        final var name = sourceName;

        sourcesGrid.setItems(query -> {
            Pageable pageable;
            if (sort == null) {
                pageable = PageRequest.of(query.getPage(), query.getPageSize());
            } else {
                pageable = PageRequest.of(query.getPage(), query.getPageSize(), sort);
            }
            return sourceService.findBySourceNameStartsWith(name, pageable).stream();
        });
    }

    private void fetchEvents(String sourceName, Sort sort) {
        if (!StringUtils.hasText(sourceName)) {
            sourceName = "";
        }
        final var name = sourceName;
        eventsGrid.setItems(query -> {
            Pageable pageable;
            if (sort == null) {
                pageable = PageRequest.of(query.getPage(), query.getPageSize());
            } else {
                pageable = PageRequest.of(query.getPage(), query.getPageSize(), sort);
            }
            return eventService.findBySourceNameStartsWith(name, pageable).stream();
        });
    }

    public void openSourceEditor(Source source) {
        addSourceBtn.setEnabled(false);
        editSourceBtn.setEnabled(false);
        sourceEditor.editSource(source);
    }

    public Sort buildSortFromSortOrderSources(GridSortOrder<Source> sortOrder) {
        var direction = sortOrder.getDirection().equals(SortDirection.DESCENDING) ? Sort.Direction.DESC : Sort.Direction.ASC;
        var property = sortOrder.getSorted().getKey();
        return Sort.by(direction, property);
    }

    public Sort buildSortFromSortOrderEvents(GridSortOrder<Event> sortOrder) {
        var direction = sortOrder.getDirection().equals(SortDirection.DESCENDING) ? Sort.Direction.DESC : Sort.Direction.ASC;
        var property = sortOrder.getSorted().getKey();
        return Sort.by(direction, property);
    }
}
