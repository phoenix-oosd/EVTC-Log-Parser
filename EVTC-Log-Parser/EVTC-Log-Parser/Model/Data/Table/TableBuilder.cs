using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EVTC_Log_Parser.Model
{
    public class TableBuilder
    {

        /* Fields */
        private int cols;
        private int[] widths;
        private string title;
        private List<string[]> rows = new List<string[]>();
        private StringBuilder output = new StringBuilder();

        /* Property */
        public string Title { set { title = " " + value + " "; } }

        /* Constructor */
        public TableBuilder(string title, string[] header)
        {
            cols = header.Length;
            this.title = ' ' + title + ' ';
            AddRow(header);
        }

        /* Public Method */
        public void AddRow(string[] row)
        {
            for (int i = 0; i < row.Length; i++)
            {
                row[i] = ' ' + row[i] + ' ';
            }
            rows.Add(row);
        }

        public void Clear()
        {
            rows = rows.Take(1).ToList();
            output.Clear();
        }

        public override string ToString()
        {
            GetWidths();
            BuildTitle();
            BuildHeader();
            BuildBody();
            string str = output.ToString();
            Clear();
            return str;
        }

        /* Private Method */
        private string CenterString(string str, int len)
        {
            return str.PadRight(((len - str.Length) / 2) + str.Length).PadLeft(len);
        }

        private string LeftAlignString(string str, int len)
        {
            return str.PadRight(len);
        }

        private string RightAlignString(string str, int len)
        {
            return str.PadLeft(len);
        }

        private void GetWidths()
        {
            widths = new int[cols];
            foreach (string[] row in rows)
            {
                for (int i = 0; i < row.Length; i++)
                {
                    widths[i] = Math.Max(widths[i], row[i].Length);
                }
            }
        }

        private void BuildTitle()
        {
            output.Append('\u250C' + "".PadRight(title.Length, '\u2500') + '\u2510' + Environment.NewLine);
            output.Append('\u2502' + title + '\u2502' + Environment.NewLine);
            output.Append('\u2514' + "".PadRight(title.Length, '\u2500') + '\u2518' + Environment.NewLine);
        }

        private void BuildHeader()
        {
            string[] header = rows.First();
            output.Append('\u250C');
            for (int colNum = 0; colNum < cols; colNum++)
            {
                output.Append("".PadRight(widths[colNum], '\u2500'));
                if (colNum != cols - 1)
                {
                    output.Append('\u252C');
                }
                else
                {
                    output.Append('\u2510' + Environment.NewLine + '\u2502');
                }
            }
            for (int colNum = 0; colNum < cols; colNum++)
            {
                output.Append(CenterString(header[colNum], widths[colNum]) + '\u2502');
            }
            output.Append(Environment.NewLine + '\u251C');
            for (int colNum = 0; colNum < cols; colNum++)
            {
                output.Append("".PadRight(widths[colNum], '\u2500'));
                if (colNum != cols - 1)
                {
                    output.Append('\u253C');
                }
                else
                {
                    output.Append('\u2524' + Environment.NewLine);
                }
            }
        }

        private void BuildBody()
        {
            foreach (string[] row in rows.Skip(1))
            {
                for (int colNum = 0; colNum < cols; colNum++)
                {
                    string text = row[colNum];
                    if (double.TryParse(text, out double result))
                    {
                        output.Append('\u2502' + RightAlignString(text, widths[colNum]));
                    }
                    else
                    {
                        output.Append('\u2502' + LeftAlignString(text, widths[colNum]));
                    }
                }
                output.Append('\u2502' + Environment.NewLine);
            }
            output.Append('\u2514');
            for (int colNum = 0; colNum < cols; colNum++)
            {
                output.Append("".PadRight(widths[colNum], '\u2500'));
                if (colNum != cols - 1)
                {
                    output.Append('\u2534');
                }
                else
                {
                    output.Append('\u2518');
                }
            }
        }

    }
}
