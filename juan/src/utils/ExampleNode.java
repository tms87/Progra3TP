/*    
    Copyright (C) 2012 http://software-talk.org/ (developer@software-talk.org)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package utils;

/**
 * A simple Example implementation of a Node only overriding the sethCosts
 * method; uses manhatten method.
 */
public class ExampleNode extends AbstractNode {

		private int densidad;
	
		public ExampleNode(int xPosition, int yPosition, int densidad) {
            super(xPosition, yPosition);
            this.densidad = densidad;
            if (densidad == 4){
            	this.setWalkable(false);
            }
            // do other init stuff
        }
        
        public void sethCosts(AbstractNode endNode) {
        	int costo = (absolute(this.getxPosition() - endNode.getxPosition()) + absolute(this.getyPosition() - endNode.getyPosition())) * BASICMOVEMENTCOST ;
        	this.sethCosts(costo * (this.densidad + 1));
        }

        private int absolute(int a) {
            return a > 0 ? a : -a;
        }

}
