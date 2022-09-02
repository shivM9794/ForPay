package in.forpay.model.shop;

public class SelectedModel {

    private int position;
    private boolean isSelected;

    public SelectedModel(int position, boolean isSelected) {
        this.position = position;
        this.isSelected = isSelected;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
