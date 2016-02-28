package club.ubly.ubly;

import dagger.Subcomponent;

@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(HomeActivity homeActivity);
}
