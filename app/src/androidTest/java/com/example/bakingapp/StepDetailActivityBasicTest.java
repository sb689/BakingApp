package com.example.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.bakingapp.model.Recipe;
import com.google.gson.Gson;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class StepDetailActivityBasicTest {


    @Rule
    public ActivityTestRule<StepDetailActivity> stepDetailTestingRule =
            new ActivityTestRule<StepDetailActivity>(StepDetailActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
                    Intent result = new Intent(targetContext, StepDetailActivity.class);
                    String recipeStr = "{\"id\":1,\"image\":\"\",\"ingredients\":[{\"ingredient\":\"Graham Cracker crumbs\",\"measure\":\"CUP\",\"quantity\":2.0},{\"ingredient\":\"unsalted butter, melted\",\"measure\":\"TBLSP\",\"quantity\":6.0},{\"ingredient\":\"granulated sugar\",\"measure\":\"CUP\",\"quantity\":0.5},{\"ingredient\":\"salt\",\"measure\":\"TSP\",\"quantity\":1.5},{\"ingredient\":\"vanilla\",\"measure\":\"TBLSP\",\"quantity\":5.0},{\"ingredient\":\"Nutella or other chocolate-hazelnut spread\",\"measure\":\"K\",\"quantity\":1.0},{\"ingredient\":\"Mascapone Cheese(room temperature)\",\"measure\":\"G\",\"quantity\":500.0},{\"ingredient\":\"heavy cream(cold)\",\"measure\":\"CUP\",\"quantity\":1.0},{\"ingredient\":\"cream cheese(softened)\",\"measure\":\"OZ\",\"quantity\":4.0}],\"name\":\"Nutella Pie\",\"servings\":8,\"steps\":[{\"description\":\"Recipe Introduction\",\"id\":0,\"shortDescription\":\"Recipe Introduction\",\"thumbnailURL\":\"\",\"videoURL\":\"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4\"},{\"description\":\"1. Preheat the oven to 350°F. Butter a 9\\\" deep dish pie pan.\",\"id\":1,\"shortDescription\":\"Starting prep\",\"thumbnailURL\":\"\",\"videoURL\":\"\"},{\"description\":\"2. Whisk the graham cracker crumbs, 50 grams (1/4 cup) of sugar, and 1/2 teaspoon of salt together in a medium bowl. Pour the melted butter and 1 teaspoon of vanilla into the dry ingredients and stir together until evenly mixed.\",\"id\":2,\"shortDescription\":\"Prep the cookie crust.\",\"thumbnailURL\":\"\",\"videoURL\":\"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd9a6_2-mix-sugar-crackers-creampie/2-mix-sugar-crackers-creampie.mp4\"},{\"description\":\"3. Press the cookie crumb mixture into the prepared pie pan and bake for 12 minutes. Let crust cool to room temperature.\",\"id\":3,\"shortDescription\":\"Press the crust into baking form.\",\"thumbnailURL\":\"\",\"videoURL\":\"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd9cb_4-press-crumbs-in-pie-plate-creampie/4-press-crumbs-in-pie-plate-creampie.mp4\"},{\"description\":\"4. Beat together the nutella, mascarpone, 1 teaspoon of salt, and 1 tablespoon of vanilla on medium speed in a stand mixer or high speed with a hand mixer until fluffy.\",\"id\":4,\"shortDescription\":\"Start filling prep\",\"thumbnailURL\":\"\",\"videoURL\":\"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd97a_1-mix-marscapone-nutella-creampie/1-mix-marscapone-nutella-creampie.mp4\"},{\"description\":\"5. Beat the cream cheese and 50 grams (1/4 cup) of sugar on medium speed in a stand mixer or high speed with a hand mixer for 3 minutes. Decrease the speed to medium-low and gradually add in the cold cream. Add in 2 teaspoons of vanilla and beat until stiff peaks form.\",\"id\":5,\"shortDescription\":\"Finish filling prep\",\"thumbnailURL\":\"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffda20_7-add-cream-mix-creampie/7-add-cream-mix-creampie.mp4\",\"videoURL\":\"\"},{\"description\":\"6. Pour the filling into the prepared crust and smooth the top. Spread the whipped cream over the filling. Refrigerate the pie for at least 2 hours. Then it\\u0027s ready to serve!\",\"id\":6,\"shortDescription\":\"Finishing Steps\",\"thumbnailURL\":\"\",\"videoURL\":\"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffda45_9-add-mixed-nutella-to-crust-creampie/9-add-mixed-nutella-to-crust-creampie.mp4\"}]}";
                    Gson gson = new Gson();
                    Recipe recipe = gson.fromJson(recipeStr, Recipe.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(targetContext.getString(R.string.bundle_extra_recipe_obj), recipe);
                    bundle.putInt(targetContext.getString(R.string.bundle_extra_step_position), 0);
                    bundle.putString(targetContext.getString(R.string.bundle_extra_recipe_step_key), targetContext.getString(R.string.bundle_extra_value_step));
                    result.putExtras(bundle);

                    return result;
                }
            };



    @Test
    public void clickNextArrow_showsNextStep(){
        onView(withId(R.id.button_next_step)).perform(click());
        onView(withId(R.id.tv_step_detail_description)).check(matches(isDisplayed()));
    }

    @Test
    public void clickPreviousArrow_showsPreviousStep(){
        onView(withId(R.id.button_next_step)).perform(click());
        onView(withId(R.id.button_prev_step)).perform(click());
        onView(withId(R.id.tv_step_detail_description)).check(matches(isDisplayed()));
    }

    @Test
    public void checkFirstStep_doesNotShowBackArrow(){
        onView(withId(R.id.button_prev_step)).check(matches(not(isDisplayed())));
    }

    @Test
    public void checkLastStep_doesNotShowNextArrow(){
       for(int i = 0; i< 6; i++){
           onView(withId(R.id.button_next_step)).perform(click());
       }
       onView(withId(R.id.button_next_step)).check(matches(not(isDisplayed())));

    }
}
