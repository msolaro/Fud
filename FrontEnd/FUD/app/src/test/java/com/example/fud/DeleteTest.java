package com.example.fud;

import com.example.fud.PantryModel.PantryList;
import com.example.fud.PantryModel.Pantry;

import org.json.JSONException;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import java.util.ArrayList;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DeleteTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();


    @Test
    public void DeleteTest() throws JSONException {
        //The mocked class that has a delete method (deleteItem())
        PantryList pantry = mock(PantryList.class);

        ArrayList<Pantry> deleted = new ArrayList<>();
        ArrayList<Pantry> original = new ArrayList();
        ArrayList<Pantry> expected = new ArrayList<>();
        ArrayList<Pantry> mocked = new ArrayList<>();

        //Original pantry
        original.add(new Pantry("oranges", "fruit", 6, "4/2/2020"));
        original.add(new Pantry("turkey", "meat", 1, "3/17/2020"));
        original.add(new Pantry("zucchini", "vegetable", 3, "3/20/2020"));
        original.add(new Pantry("hummus", "other", 1, "4/18/2019"));
        original.add(new Pantry("grapefruits", "fruit", 2, "3/30/2019"));

        //Expected pantry
        expected.add(new Pantry("oranges", "fruit", 6, "4/2/2020"));
        expected.add(new Pantry("zucchini", "vegetable", 3, "3/20/2020"));
        expected.add(new Pantry("peanut butter", "other", 1, "4/18/2019"));

        deleted.add(new Pantry("turkey", "meat", 1, "3/17/2020"));
        deleted.add(new Pantry("grapefruits", "fruit", 2, "3/30/2019"));

        //Mocks getAllEntries method
        when(pantry.getAllEntries()).thenReturn(expected);
        
        //Tests delete
        for (Pantry item: deleted) {
            pantry.removeItem(item);
        }
        mocked.addAll(pantry.getAllEntries());

        for (int i=0; i<expected.size(); i++) {
            assertSame(expected.get(i), mocked.get(i));
        }
    }
}

