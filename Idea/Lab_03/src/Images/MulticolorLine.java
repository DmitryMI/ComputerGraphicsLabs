package Images;

import java.awt.*;

public class MulticolorLine extends PixImage
{
    private Color base;
    private Color back;
    private Graphics gr;
    private Vertex start, end;
    private int levels;

    public MulticolorLine(Vertex start, Vertex end,  Color baseColor, Color backColor, Container canvas, int levels)
    {
        base = baseColor;
        back = backColor;
        this.start = start;
        this.end = end;

        gr = canvas.getGraphics();
        gr.translate(canvas.getWidth() / 2, canvas.getHeight() / 2);
        this.levels = levels;
    }

    private void plot(int x, int y, float c)
    {
        int r = base.getRed();
        int g = base.getGreen();
        int b = base.getBlue();
        r += (1-c) * (back.getRed() - r);
        g += (1-c) * (back.getGreen() - g);
        b += (1-c) * (back.getBlue() - b);

        if(c <= 1)
            gr.setColor(new Color(r, g, b));
        else
            gr.setColor(base);
        gr.drawOval(x - 1, -y - 1, 1, 1);
        y = PixImage.PixelSize * y;
    }

    private static void plot_timed(int x, int y, float c)
    {
        int r = 0;
        int g = 0;
        int b = 0;
        r += (1-c) * (255 - r);
        g += (1-c) * (255 - g);
        b += (1-c) * (255 - b);
    }

    private static int ipart(float x)
    {
        return (int)x;
    }

    private static float fpart(float x)
    {
        return Math.abs(x - ipart(x));
    }

    private static int sign1(float x)
    {
        if(x >= 0)
            return 1;
        return -1;
    }

    @Override
    /*public void draw(Container canvas)
    {
        int x0 = start.x;
        int x1 = end.x;
        int y0 = start.y;
        int y1 = end.y;
        boolean steep = Math.abs(y1 - y0) > Math.abs(x1 - x0);
        if(steep)
        {
            int t = y0;
            y0 = x0;
            x0 = t;
            t = y1;
            y1 = x1;
            x1 = t;
        }
        if(x0 > x1)
        {
            int t = x1;
            x1 = x0;
            x0 = t;
            t = y1;
            y1 = y0;
            y0 = t;
        }

        if(steep)
        {
            plot(x0, y0, 1);
            plot(x1, y1, 1);
        }
        else
        {
            plot(y0, x0, 1);
            plot(y1, x1, 1);
        }

        float dx = x1 - x0;
        float dy = y1 - y0;
        float gradient = dy/dx;
        float y = y0 + gradient;
        for(int x = x0 + 1; x < x1-1; x++)
        {
            if(steep)
            {
                plot(x, (int)y, 1 - fpart(y));
                plot(x, (int)y + sign1(y), fpart(y));
            }
            else
            {
                plot((int)y, x, 1 - fpart(y));
                plot((int)y + sign1(y), x, fpart(y));
            }
            y += gradient;
        }
    }*/

    public void draw(Container canvas)
    {
        int x = start.x;
        int y = start.y;

        int dx = end.x - start.x;
        int dy = end.y - start.y;

        int sx = (int)Math.signum(dx);
        int sy = (int)Math.signum(dy);
        dx = Math.abs(dx);
        dy = Math.abs(dy);

        int fl = 0;

        if (dy > dx)
        {
            fl = 1;
            int tmp = dx;
            dx = dy;
            dy = tmp;
        }

        float f = (float)levels / 2.0f;
        float m = (float)levels * ((float)dy / (float)dx);
        float w = (float)levels - m;

        for(int i = 0; i < dx; i++)
        {
            plot(x, y, f / (float)levels);
            if(f < w)
            {
                if (fl == 0)
                    x += sx;
                else
                    y += sy;
                f += m;
            }
            else
            {
                y += sy;
                x += sx;
                f -= w;
            }

        }

    }

    public static void buildlowStep_timed(Vertex start, Vertex end)
    {
        int x0 = start.x;
        int x1 = end.x;
        int y0 = start.y;
        int y1 = end.y;
        boolean steep = Math.abs(y1 - y0) > Math.abs(x1 - x0);
        if(steep)
        {
            int t = y0;
            y0 = x0;
            x0 = t;
            t = y1;
            y1 = x1;
            x1 = t;
        }
        if(x0 > x1)
        {
            int t = x1;
            x1 = x0;
            x0 = t;
            t = y1;
            y1 = y0;
            y0 = t;
        }

        if(steep)
        {
            plot_timed(x0, y0, 1);
            plot_timed(x1, y1, 1);
        }
        else
        {
            plot_timed(y0, x0, 1);
            plot_timed(y1, x1, 1);
        }

        float dx = x1 - x0;
        float dy = y1 - y0;
        float gradient = dy/dx;
        float y = y0 + gradient;
        for(int x = x0 + 1; x < x1-1; x++)
        {
            if(steep)
            {
                plot_timed(x, (int)y, 1 - fpart(y));
                plot_timed(x, (int)y + sign1(y), fpart(y));
            }
            else
            {
                plot_timed((int)y, x, 1 - fpart(y));
                plot_timed((int)y + sign1(y), x, fpart(y));
            }
            y += gradient;
        }
    }
}