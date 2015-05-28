package connect4;

public enum Mode
{
    NORMAL, SPEED, ENDLESS, TUTORIAL;

    public boolean endless()
    {
        return this == ENDLESS || this == TUTORIAL;
    }

    public boolean tutorial()
    {
        return this == TUTORIAL;
    }
}
